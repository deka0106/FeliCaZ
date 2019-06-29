package work.deka.zaim.util

import java.text.SimpleDateFormat
import java.util.*

private val format = SimpleDateFormat("yyyy-MM-dd")
fun format(date: Date?) = format.format(date) ?: ""