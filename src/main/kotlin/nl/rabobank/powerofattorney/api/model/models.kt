package nl.rabobank.powerofattorney.api.model

data class Account(val owner: String,
                   val balance: String,
                   val created: String,
                   val ended: String?)

enum class CardType {
    DEBIT_CARD,
    CREDIT_CARD
}

enum class CardStatus {
    ACTIVE,
    BLOCKED
}

data class CardLimit(val limit: Long,
                     val periodUnit: String)

data class Card(val id: String,
                val type: CardType)

data class CreditCard(val id: String,
                      val status: CardStatus,
                      val cardNumber: Long,
                      val sequenceNumber: Int,
                      val cardHolder: String,
                      val monthlyLimit: String)

data class DebitCard(val id: String,
                     val status: CardStatus,
                     val cardNumber: Long,
                     val sequenceNumber: Int,
                     val cardHolder: String,
                     val atmLimit: CardLimit,
                     val posLimit: CardLimit,
                     val contactless: Boolean)

enum class AccountAuthorization {
    DEBIT_CARD,
    CREDIT_CARD,
    VIEW,
    PAYMENT
}

data class PowerOfAttorney(val id: String,
                           val grantor: String,
                           val grantee: String,
                           val account: String,
                           val direction: String,
                           val authorizations: List<AccountAuthorization>,
                           val cards: List<Card> = listOf())

data class PowerOfAttorneyList(val id: String)


data class AccountDetails(val userId: String,
                          var account: Account? = null,
                          var creditCards: List<CreditCard>? = null,
                          var debitCards: List<DebitCard>? = null)