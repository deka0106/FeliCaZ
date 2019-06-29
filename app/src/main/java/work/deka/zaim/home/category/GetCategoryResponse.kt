package work.deka.zaim.home.category

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import work.deka.zaim.ZaimResponse
import work.deka.zaim.home.Mode
import java.util.*

data class GetCategoryResponse(
    val categories: List<Category>,
    val requested: Int
) : ZaimResponse {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class Category(
        val id: Long,
        val name: String,
        val mode: Mode,
        val sort: Int,
        val parentCategoryId: Long,
        val localId: Long,
        val active: Int,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val modified: Date
    )
}