package nl.rabobank.powerofattorney.api.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StubConfiguration {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder) = builder
            .build()
}