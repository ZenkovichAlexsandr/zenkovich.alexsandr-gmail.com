package nl.rabobank.powerofattorney.api.controller

import nl.rabobank.powerofattorney.api.model.AccountDetails
import nl.rabobank.powerofattorney.api.service.AccountDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("account-details")
class AccountDetailsController {
    @Autowired
    private lateinit var service: AccountDetailsService

    @GetMapping("/{userId}")
    fun get(@PathVariable userId: String): List<AccountDetails> {
        val start = System.currentTimeMillis()
        val details = service.getDetails(userId)
        System.out.println(System.currentTimeMillis() - start)

        return details
    }
}