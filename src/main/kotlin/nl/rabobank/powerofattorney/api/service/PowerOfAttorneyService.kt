package nl.rabobank.powerofattorney.api.service

import nl.rabobank.powerofattorney.api.model.PowerOfAttorney
import nl.rabobank.powerofattorney.api.model.PowerOfAttorneyList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.beans.factory.annotation.Value
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.allOf

@Service
class PowerOfAttorneyService {
    @Value("\${stup.powerOfAttorneys}")
    private lateinit var url: String
    @Autowired
    private lateinit var restTemplate: RestTemplate

    fun findByUser(userId: String): List<PowerOfAttorney> {
        val list = restTemplate
                .getForObject(url, Array<PowerOfAttorneyList>::class.java)
                ?.map { findById(it.id) }

        return list?.let { value ->
            allOf(*value.toTypedArray())
                    .thenApply {
                        value.map { it.join() }
                    }.join()
                    .filterNotNull()
                    .filter { it.grantee == userId }
        } ?: listOf()
    }

    private fun findById(id: String) = CompletableFuture.supplyAsync {
        restTemplate.getForObject("$url/$id", PowerOfAttorney::class.java)
    }
}