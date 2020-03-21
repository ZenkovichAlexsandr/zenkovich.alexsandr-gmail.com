package nl.rabobank.powerofattorney.api.service

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import nl.rabobank.powerofattorney.api.model.AccountDetails
import nl.rabobank.powerofattorney.api.model.CardType
import nl.rabobank.powerofattorney.api.model.PowerOfAttorney
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountDetailsService {
    @Autowired
    private lateinit var powerOfAttorneyService: PowerOfAttorneyService
    @Autowired
    private lateinit var cardService: CardService
    @Autowired
    private lateinit var accountService: AccountService

    fun getDetails(userId: String) = runBlocking {
        powerOfAttorneyService.findByUser(userId)
                .map { async { buildAccountDetails(userId, it) } }
                .awaitAll()
                .filterNotNull()
    }

    private suspend fun buildAccountDetails(userId: String, powerOfAttorney: PowerOfAttorney) = coroutineScope {
        val account = accountService.getAccount(powerOfAttorney.account)

        if (account?.ended != null) {
            return@coroutineScope null
        }

        val details = AccountDetails(id = powerOfAttorney.id,
                grantor = powerOfAttorney.grantor,
                direction = powerOfAttorney.direction,
                authorizations = powerOfAttorney.authorizations,
                userId = userId,
                account = account)

        loadCards(details, powerOfAttorney)
        details
    }

    private suspend fun loadCards(details: AccountDetails,
                                  powerOfAttorney: PowerOfAttorney) = coroutineScope {
        val cardsInfo = powerOfAttorney.cards.groupBy({ it.type }, { it.id })

        val creditCards = async {
            cardService.getCreditCards(cardsInfo[CardType.CREDIT_CARD])
        }
        val debitCards = async {
            cardService.getDebitCards(cardsInfo[CardType.DEBIT_CARD])
        }

        details.creditCards = creditCards.await().filterNotNull()
        details.debitCards = debitCards.await().filterNotNull()
    }
}
