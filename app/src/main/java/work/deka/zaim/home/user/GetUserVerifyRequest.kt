package work.deka.zaim.home.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import work.deka.zaim.ZaimRequest
import work.deka.zaim.Zaim
import work.deka.zaim.exception.ZaimException

class GetUserVerifyRequest(
    private val zaim: Zaim
) : ZaimRequest<GetUserVerifyResponse> {

    override fun execute(): GetUserVerifyResponse {
        val url = zaim.buildUrl("/v2/home/user/verify")
        val response = zaim.request().buildGetRequest(url).execute()
        if (response.isSuccessStatusCode) return jacksonObjectMapper().readValue(response.parseAsString())
        else throw ZaimException("Failed to request: $url by ${response.statusCode} ${response.statusMessage}")
    }

}