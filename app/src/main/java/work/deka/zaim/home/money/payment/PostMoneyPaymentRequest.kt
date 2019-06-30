package work.deka.zaim.home.money.payment

import work.deka.zaim.Zaim
import work.deka.zaim.home.Mode
import work.deka.zaim.home.money.PostMoneyRequest
import work.deka.zaim.util.format
import java.util.*

class PostMoneyPaymentRequest(
    override val zaim: Zaim,
    var mapping: Int,
    var categoryId: Long,
    var genreId: Long,
    var amount: Int,
    var date: Date
) : PostMoneyRequest {

    var fromAccountId: Long? = null
    var comment: String? = null
    var name: String? = null
    var place: String? = null

    override val mode = Mode.PAYMENT
    override val params
        get() = HashMap<String, Any?>().apply {
            put("mapping", mapping)
            put("category_id", categoryId)
            put("genre_id", genreId)
            put("amount", amount)
            put("date", format(date))
            if (fromAccountId != null) put("from_account_id", fromAccountId)
            if (comment != null) put("comment", comment)
            if (name != null) put("name", name)
            if (place != null) put("place", place)
        }

}