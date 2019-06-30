package work.deka.zaim.home.money.income

import work.deka.zaim.Zaim
import work.deka.zaim.home.Mode
import work.deka.zaim.home.money.PostMoneyRequest
import work.deka.zaim.util.format
import java.util.*

class PostMoneyIncomeRequest(
    override val zaim: Zaim,
    var mapping: Int,
    var categoryId: Long,
    var amount: Int,
    var date: Date
) : PostMoneyRequest {

    var toAccountId: Long? = null
    var place: String? = null
    var comment: String? = null

    override val mode = Mode.INCOME
    override val params
        get() = HashMap<String, Any?>().apply {
            put("mapping", mapping)
            put("category_id", categoryId)
            put("amount", amount)
            put("date", format(date))
            if (toAccountId != null) put("to_account_id", toAccountId)
            if (place != null) put("place", place)
            if (comment != null) put("comment", comment)
        }

}