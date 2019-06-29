package work.deka.zaim.home.genre

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import work.deka.zaim.ZaimResponse
import java.util.*

data class GetGenreResponse(
    val genres: List<Genre>,
    val requested: Int
) : ZaimResponse {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class Genre(
        val id: Long,
        val name: String,
        val sort: Int,
        val categoryId: Long,
        val parentGenreId: Long,
        val localId: Long,
        val active: Int,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val modified: Date
    )
}