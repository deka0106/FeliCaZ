package work.deka.zaim.home.user

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import work.deka.zaim.Response
import java.util.*

data class GetUserVerifyResponse(
    val me: Me,
    val requested: Int
) : Response {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class Me(
        val login: String,
        val inputCount: Int,
        val dayCount: Int,
        val repeatCount: Int,
        val id: Int,
        val currencyCode: String,
        val week: Int,
        val month: Int,
        val active: Int,
        val day: Int,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val profileModified: Date,
        val name: String,
        val profileImageUrl: String,
        val coverImageUrl: String
    )
}