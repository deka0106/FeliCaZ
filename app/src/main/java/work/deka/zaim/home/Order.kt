package work.deka.zaim.home

import com.fasterxml.jackson.annotation.JsonValue


enum class Order {
    ID, DATE;

    @JsonValue
    fun value() = name.toLowerCase()

    override fun toString() = value()
}