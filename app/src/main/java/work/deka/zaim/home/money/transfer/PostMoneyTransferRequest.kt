package work.deka.zaim.home.money.transfer

import com.google.api.client.http.HttpContent
import com.google.api.client.http.json.JsonHttpContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.GenericData
import work.deka.zaim.Zaim
import work.deka.zaim.home.Mode
import work.deka.zaim.home.money.PostMoneyRequest
import work.deka.zaim.util.format
import java.util.*

class PostMoneyTransferRequest(
    zaim: Zaim,
    var mapping: Int,
    var amount: Int,
    var date: Date,
    var fromAccountId: Long,
    var toAccountId: Long
) : PostMoneyRequest(zaim, Mode.TRANSFER) {

    var comment: String? = null

    override fun buildData() = GenericData().apply {
        set("mapping", mapping)
        set("amount", amount)
        set("date", format(date))
        set("from_account_id", fromAccountId)
        set("to_account_id", toAccountId)
        if (comment != null) set("comment", comment)
    }

}