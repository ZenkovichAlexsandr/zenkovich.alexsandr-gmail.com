package nl.rabobank.powerofattorney.api.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import nl.rabobank.powerofattorney.api.model.Account
import java.util.concurrent.CompletableFuture.supplyAsync

const val ACCOUNT_PREFIX = "NL23RABO"

@Service
class AccountService {
    @Value("\${stup.accounts}")
    private lateinit var accountsUrl: String
    @Autowired
    private lateinit var restTemplate: RestTemplate

    fun getAccount(accountNumber: String) = supplyAsync {
        restTemplate.getForObject("$accountsUrl/${accountNumber.drop(ACCOUNT_PREFIX.length)}", Account::class.java)
    }
}