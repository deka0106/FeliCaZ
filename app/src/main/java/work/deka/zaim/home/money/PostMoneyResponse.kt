package work.deka.zaim.home.money

import work.deka.zaim.ZaimResponse
import work.deka.zaim.home.money.Money
import work.deka.zaim.home.money.User

data class PostMoneyResponse(
    val money: Money,
    val place: Place? = null,
    val user: User,
    val requested: Int
) : ZaimResponse