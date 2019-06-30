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
        val path = "/v2/home/user/verify"
        val response = zaim.get(path)
        if (response.statusLine.statusCode == 200) return jacksonObjectMapper().readValue(response.entity.content)
        else throw ZaimException("Failed to request $path by ${response.statusLine.statusCode} ${response.statusLine.reasonPhrase}")
    }

}