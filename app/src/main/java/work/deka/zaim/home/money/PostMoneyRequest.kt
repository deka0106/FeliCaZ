package work.deka.zaim.home.money

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import work.deka.zaim.Zaim
import work.deka.zaim.ZaimRequest
import work.deka.zaim.exception.ZaimException
import work.deka.zaim.home.Mode

interface PostMoneyRequest : ZaimRequest<PostMoneyResponse> {

    val zaim: Zaim
    val mode: Mode
    val params: Map<String, Any?>

    override fun execute(): PostMoneyResponse {
        val path = "/v2/home/money/$mode"
        val response = zaim.post(path, params)
        if (response.statusLine.statusCode == 200) return jacksonObjectMapper().readValue(response.entity.content)
        else throw ZaimException("Failed to request $path by ${response.statusLine.statusCode} ${response.statusLine.reasonPhrase}")
    }

}