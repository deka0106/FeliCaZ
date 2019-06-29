package work.deka.zaim.home.money.payment

import com.google.api.client.util.GenericData
import work.deka.zaim.Zaim
import work.deka.zaim.home.Mode
import work.deka.zaim.home.money.PostMoneyRequest
import work.deka.zaim.util.format
import java.util.*

class PostMoneyPaymentRequest(
    zaim: Zaim,
    var mapping: Int,
    var categoryId: Long,
    var genreId: Long,
    var amount: Int,
    var date: Date
) : PostMoneyRequest(zaim, Mode.PAYMENT) {

    var fromAccountId: Long? = null
    var comment: String? = null
    var name: String? = null
    var place: String? = null

    override fun buildData() = GenericData().apply {
        set("mapping", mapping)
        set("category_id", categoryId)
        set("genre_id", genreId)
        set("amount", amount)
        set("date", format(date))
        if (fromAccountId != null) set("from_account_id", fromAccountId)
        if (comment != null) set("comment", comment)
        if (name != null) set("name", name)
        if (place != null) set("place", place)
    }

}