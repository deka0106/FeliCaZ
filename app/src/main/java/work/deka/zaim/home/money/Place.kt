package work.deka.zaim.home.money

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import work.deka.zaim.home.Mode
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Place(
    val id: Long,
    val userId: Long,
    val categoryId: Long,
    val accountId: Long,
    val transferAccountId: Long,
    val mode: Mode,
    val placeUid: String,
    val service: String,
    val name: String,
    val originalName: String,
    val tel: String,
    val count: Int,
    val placePatternId: Long,
    val calcFlag: Int,
    val editFlag: Int,
    val active: Int,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val modified: Date,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val created: Date
)