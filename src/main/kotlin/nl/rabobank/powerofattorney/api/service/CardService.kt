package nl.rabobank.powerofattorney.api.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import nl.rabobank.powerofattorney.api.model.CreditCard
import nl.rabobank.powerofattorney.api.model.DebitCard
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CardService {
    @Value("\${stup.creditCards}")
    private lateinit var creditCardsUrl: String
    @Value("\${stup.debitCards}")
    private lateinit var debitCardsUrl: String
    @Autowired
    private lateinit var restTemplate: RestTemplate

    suspend fun getCreditCards(ids: List<String>?) = findAll(ids, creditCardsUrl, CreditCard::class.java)
    suspend fun getDebitCards(ids: List<String>?) = findAll(ids, debitCardsUrl, DebitCard::class.java)

    private suspend fun <T : Any> findAll(ids: List<String>?, url: String, clazz: Class<T>): List<T?> = coroutineScope {
        ids?.map { id ->
            async(Dispatchers.IO) {
                restTemplate.getForObject("$url/$id", clazz)
            }
        }?.awaitAll() ?: listOf()
    }
}
