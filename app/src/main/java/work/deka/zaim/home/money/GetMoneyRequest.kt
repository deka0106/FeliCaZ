package work.deka.zaim.home.money

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.api.client.http.GenericUrl
import work.deka.zaim.Zaim
import work.deka.zaim.ZaimRequest
import work.deka.zaim.exception.ZaimException
import work.deka.zaim.home.Mode
import work.deka.zaim.home.Order
import work.deka.zaim.util.format
import java.text.SimpleDateFormat
import java.util.*

class GetMoneyRequest(
    private val zaim: Zaim
) : ZaimRequest<GetMoneyResponse> {

    var mapping: Int = 1
    var categoryId: Long? = null
    var genreId: Long? = null
    var mode: Mode? = null
    var order: Order? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var page: Int? = null
    var limit: Int? = null
    var groupBy: String? = null

    override fun execute(): GetMoneyResponse {
        val url = buildUrl()
        val response = zaim.request().buildGetRequest(url).execute()
        if (response.isSuccessStatusCode) return jacksonObjectMapper().readValue(response.parseAsString())
        else throw ZaimException("Failed to request: $url by ${response.statusCode} ${response.statusMessage}")
    }

    private fun buildUrl(): GenericUrl = zaim.buildUrl("/v2/home/money").apply {
        set("mapping", mapping)
        if (categoryId != null) set("category_id", categoryId)
        if (genreId != null) set("genre_id", genreId)
        if (mode != null) set("mode", mode)
        if (order != null) set("order", order)
        if (startDate != null) set("start_date", format(startDate))
        if (endDate != null) set("end_date", format(endDate))
        if (page != null) set("page", page)
        if (limit != null) set("limit", limit)
        if (groupBy != null) set("group_by", groupBy)
    }

}