package nl.rabobank.powerofattorney.api.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
    }

    private suspend fun buildAccountDetails(userId: String, powerOfAttorney: PowerOfAttorney) = coroutineScope {
        val cardsInfo = powerOfAttorney.cards.groupBy({ it.type }, { it.id })
        val details = AccountDetails(userId)

        val account = async {
            accountService.getAccount(powerOfAttorney.account)
        }
        val creditCards = async {
            cardService.getCreditCards(cardsInfo[CardType.CREDIT_CARD])
        }
        val debitCards = async {
            cardService.getDebitCards(cardsInfo[CardType.DEBIT_CARD])
        }

        details.account = account.await()
        details.creditCards = creditCards.await().filterNotNull()
        details.debitCards = debitCards.await().filterNotNull()
        details
    }
}
