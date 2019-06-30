package work.deka.zaim.home.money

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import work.deka.zaim.Zaim
import work.deka.zaim.ZaimRequest
import work.deka.zaim.exception.ZaimException
import work.deka.zaim.home.Mode
import work.deka.zaim.home.Order
import work.deka.zaim.util.format
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
        val path = "/v2/home/money"
        val response = zaim.get(path, params)
        if (response.statusLine.statusCode == 200) return jacksonObjectMapper().readValue(response.entity.content)
        else throw ZaimException("Failed to request $path by ${response.statusLine.statusCode} ${response.statusLine.reasonPhrase}")
    }

    private val params = HashMap<String, Any?>().apply {
        put("mapping", mapping)
        if (categoryId != null) put("category_id", categoryId)
        if (genreId != null) put("genre_id", genreId)
        if (mode != null) put("mode", mode)
        if (order != null) put("order", order)
        if (startDate != null) put("start_date", format(startDate))
        if (endDate != null) put("end_date", format(endDate))
        if (page != null) put("page", page)
        if (limit != null) put("limit", limit)
        if (groupBy != null) put("group_by", groupBy)
    }

}