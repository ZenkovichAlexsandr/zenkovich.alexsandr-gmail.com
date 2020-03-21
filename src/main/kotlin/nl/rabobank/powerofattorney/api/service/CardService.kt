package nl.rabobank.powerofattorney.api.service

import nl.rabobank.powerofattorney.api.model.CreditCard
import nl.rabobank.powerofattorney.api.model.DebitCard
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.allOf
import java.util.concurrent.CompletableFuture.completedFuture
import java.util.concurrent.CompletableFuture.supplyAsync

@Service
class CardService {
    @Value("\${stup.creditCards}")
    private lateinit var creditCardsUrl: String
    @Value("\${stup.debitCards}")
    private lateinit var debitCardsUrl: String
    @Autowired
    private lateinit var restTemplate: RestTemplate

    fun getCreditCards(ids: List<String>?) = findAll(ids, creditCardsUrl, CreditCard::class.java)
    fun getDebitCards(ids: List<String>?) = findAll(ids, debitCardsUrl, DebitCard::class.java)

    private fun <T : Any> findAll(ids: List<String>?, url: String, clazz: Class<T>): CompletableFuture<List<T?>> {
        val list = ids?.map { findById(url, it, clazz) }

        return list?.let { value ->
            allOf(*value.toTypedArray())
                    .thenApply {
                        value.map { it.join() }
                    }
        } ?: completedFuture(listOf())
    }

    private fun <T> findById(url: String, id: String, clazz: Class<T>) = supplyAsync {
        restTemplate.getForObject("$url/$id", clazz)
    }
}