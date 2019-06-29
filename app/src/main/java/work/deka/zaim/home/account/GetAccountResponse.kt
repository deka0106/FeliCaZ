package work.deka.zaim.home.account

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import work.deka.zaim.ZaimResponse
import work.deka.zaim.home.Mode
import java.util.*

data class GetAccountResponse(
    val accounts: List<Account>,
    val requested: Int
) : ZaimResponse {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class Account(
        val id: Long,
        val name: String,
        val mode: Mode? = null,
        val sort: Int,
        val parentAccountId: Long,
        val localId: Long,
        val websiteId: Long,
        val originalName: String? = null,
        val active: Int,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val modified: Date
    )
}