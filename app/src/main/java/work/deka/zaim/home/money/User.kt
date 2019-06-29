package work.deka.zaim.home.money

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class User(
    val repeatCount: Int,
    val dayCount: Int,
    val inputCount: Int,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val dataModified: Date? = null
)