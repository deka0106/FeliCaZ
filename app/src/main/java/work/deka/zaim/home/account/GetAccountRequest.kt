package work.deka.zaim.home.account

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import work.deka.zaim.Zaim
import work.deka.zaim.ZaimRequest
import work.deka.zaim.exception.ZaimException

class GetAccountRequest(
    private val zaim: Zaim
) : ZaimRequest<GetAccountResponse> {

    override fun execute(): GetAccountResponse {
        val path = "/v2/home/account"
        val response = zaim.get(path)
        if (response.statusLine.statusCode == 200) return jacksonObjectMapper().readValue(response.entity.content)
        else throw ZaimException("Failed to request $path by ${response.statusLine.statusCode} ${response.statusLine.reasonPhrase}")
    }

}