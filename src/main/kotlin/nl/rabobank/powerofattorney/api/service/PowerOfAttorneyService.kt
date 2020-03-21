package nl.rabobank.powerofattorney.api.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import nl.rabobank.powerofattorney.api.model.PowerOfAttorney
import nl.rabobank.powerofattorney.api.model.PowerOfAttorneyList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.beans.factory.annotation.Value

@Service
class PowerOfAttorneyService {
    @Value("\${stup.powerOfAttorneys}")
    private lateinit var url: String
    @Autowired
    private lateinit var restTemplate: RestTemplate

    suspend fun findByUser(userId: String) = coroutineScope {
        restTemplate
                .getForObject(url, Array<PowerOfAttorneyList>::class.java)
                ?.map {
                    async(Dispatchers.IO) {
                        restTemplate.getForObject("$url/${it.id}", PowerOfAttorney::class.java)
                    }
                }?.awaitAll()
                ?.filterNotNull()
                ?.filter { it.grantee == userId } ?: listOf()
    }
}