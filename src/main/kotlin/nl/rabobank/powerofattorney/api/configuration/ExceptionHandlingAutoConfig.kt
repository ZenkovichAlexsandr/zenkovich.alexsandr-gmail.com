package nl.rabobank.powerofattorney.api.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.ResourceAccessException

@Configuration
@ControllerAdvice
class ExceptionHandlingAutoConfig {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handle(exception: Exception): ResponseEntity<ExceptionErrorInfo> {
        return ResponseEntity(
                ExceptionErrorInfo(getCode(exception), exception.message),
                HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun getCode(exception: Exception): String {
        if (exception is ResourceAccessException) {
            return "STUB_SERVER_ERROR"
        }
        return "UNEXPECTED"
    }
}

data class ExceptionErrorInfo(val code: String, val message: String?)