package nl.rabobank.powerofattorney.api.service

import nl.rabobank.powerofattorney.api.model.AccountDetails
import nl.rabobank.powerofattorney.api.model.CardType
import nl.rabobank.powerofattorney.api.model.PowerOfAttorney
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture.allOf

@Service
class AccountDetailsService {
    @Autowired
    private lateinit var powerOfAttorneyService: PowerOfAttorneyService
    @Autowired
    private lateinit var cardService: CardService
    @Autowired
    private lateinit var accountService: AccountService

    fun getDetails(userId: String) = powerOfAttorneyService.findByUser(userId)
            .map { buildAccountDetails(userId, it) }

    private fun buildAccountDetails(userId: String, powerOfAttorney: PowerOfAttorney): AccountDetails {
        val cardsInfo = powerOfAttorney.cards.groupBy({ it.type }, { it.id })
        val details = AccountDetails(userId)

        allOf(
                accountService.getAccount(powerOfAttorney.account).thenAccept {
                    details.account = it
                },
                cardService.getCreditCards(cardsInfo[CardType.CREDIT_CARD]).thenAccept {
                    details.creditCards = it.filterNotNull()
                },
                cardService.getDebitCards(cardsInfo[CardType.DEBIT_CARD]).thenAccept {
                    details.debitCards = it.filterNotNull()
                }
        )

        return details
    }
}