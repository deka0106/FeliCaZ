package work.deka.felicaz.history

import android.content.Context
import work.deka.nfc.felica.suica.Stations
import work.deka.nfc.felica.suica.Suica
import work.deka.zaim.home.Mode
import java.util.*

data class History(
    val data: ByteArray,
    val mode: Mode,
    var name: String,
    var comment: String,
    var categoryId: Long,
    var genreId: Long,
    var amount: Int,
    var balance: Int,
    var date: Date
) {

    var checked: Boolean = true

    override fun equals(other: Any?): Boolean = other is History && data.contentEquals(other.data)
    override fun hashCode(): Int = data.contentHashCode()

    companion object {
        fun fromEntries(context: Context, entries: List<Suica.Entry>): List<History> {
            val stations = Stations(context)
            val list = ArrayList<History>()
            for (i in 0 until entries.size - 1) {
                val entry = entries[i]
                val amount = entry.balance - entries[i + 1].balance
                if (entry.isStation) {
                    val comment = "${
                    stations.get(entry.inAreaCode, entry.inLineCode, entry.inStationCode)?.stationName
                    } → ${
                    stations.get(entry.outAreaCode, entry.outLineCode, entry.outStationCode)?.stationName
                    }"
                    list.add(
                        // Income
                        if (0 < amount) {
                            History(
                                entry.data,
                                Mode.INCOME,
                                entry.process,
                                comment,
                                199, 19908,
                                amount,
                                entry.balance,
                                entry.date
                            )
                        }
                        // Payment
                        else {
                            History(
                                entry.data,
                                Mode.PAYMENT,
                                entry.process,
                                comment,
                                103, 10301,
                                -amount,
                                entry.balance,
                                entry.date
                            )
                        }
                    )
                }
            }
            return list
        }
    }

}
