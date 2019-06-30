package work.deka.zaim.home.money.transfer

import work.deka.zaim.Zaim
import work.deka.zaim.home.Mode
import work.deka.zaim.home.money.PostMoneyRequest
import work.deka.zaim.util.format
import java.util.*

class PostMoneyTransferRequest(
    override val zaim: Zaim,
    var mapping: Int,
    var amount: Int,
    var date: Date,
    var fromAccountId: Long,
    var toAccountId: Long
) : PostMoneyRequest {

    var comment: String? = null

    override val mode = Mode.TRANSFER
    override val params
        get() = HashMap<String, Any?>().apply {
            set("mapping", mapping)
            set("amount", amount)
            set("date", format(date))
            set("from_account_id", fromAccountId)
            set("to_account_id", toAccountId)
            if (comment != null) set("comment", comment)
        }

}