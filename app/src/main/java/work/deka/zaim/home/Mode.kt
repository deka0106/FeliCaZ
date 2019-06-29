package work.deka.zaim.home

import com.fasterxml.jackson.annotation.JsonValue

enum class Mode {
    PAYMENT, INCOME, TRANSFER;

    @JsonValue
    fun value() = name.toLowerCase()

    override fun toString() = value()
}