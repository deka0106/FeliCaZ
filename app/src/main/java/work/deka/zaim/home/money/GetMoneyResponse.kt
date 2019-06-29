package work.deka.zaim.home.money

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import work.deka.zaim.ZaimResponse
import java.util.*

data class GetMoneyResponse(
    val money: List<Money>,
    val requested: Int
) : ZaimResponse {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class Money(
        val id: Long,
        val mode: String,
        val userId: Long,
        @JsonFormat(pattern = "yyyy-MM-dd")
        val date: Date,
        val categoryId: Long,
        val genreId: Long,
        val toAccountId: Long,
        val fromAccountId: Long,
        val amount: Int,
        val comment: String,
        val active: Int,
        val name: String,
        val receiptId: Long,
        val placeUid: String,
        val place: String,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val created: Date,
        val currencyCode: String
    )
}