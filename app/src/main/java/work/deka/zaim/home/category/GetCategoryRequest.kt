package work.deka.zaim.home.category

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import work.deka.zaim.ZaimRequest
import work.deka.zaim.Zaim
import work.deka.zaim.exception.ZaimException

class GetCategoryRequest(
    private val zaim: Zaim
) : ZaimRequest<GetCategoryResponse> {

    override fun execute(): GetCategoryResponse {
        val url = zaim.buildUrl("/v2/home/category")
        val response = zaim.request().buildGetRequest(url).execute()
        if (response.isSuccessStatusCode) return jacksonObjectMapper().readValue(response.parseAsString())
        else throw ZaimException("Failed to request: $url by ${response.statusCode} ${response.statusMessage}")
    }

}