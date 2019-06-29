package work.deka.zaim.home.money

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Money(
    val id: Long,
    val placeUid: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val modified: Date
)