package work.deka.zaim.home.money.income

import com.google.api.client.http.HttpContent
import com.google.api.client.http.json.JsonHttpContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.GenericData
import work.deka.zaim.Zaim
import work.deka.zaim.home.Mode
import work.deka.zaim.home.money.PostMoneyRequest
import work.deka.zaim.util.format
import java.util.*

class PostMoneyIncomeRequest(
    zaim: Zaim,
    var mapping: Int,
    var categoryId: Long,
    var amount: Int,
    var date: Date
) : PostMoneyRequest(zaim, Mode.INCOME) {

    var toAccountId: Long? = null
    var place: String? = null
    var comment: String? = null

    override fun buildData() = GenericData().apply {
        set("mapping", mapping)
        set("category_id", categoryId)
        set("amount", amount)
        set("date", format(date))
        if (toAccountId != null) set("to_account_id", toAccountId)
        if (place != null) set("place", place)
        if (comment != null) set("comment", comment)
    }

}