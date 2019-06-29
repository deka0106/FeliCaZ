package work.deka.nfc.felica.suica

import android.content.Context
import com.univocity.parsers.annotations.Parsed
import com.univocity.parsers.common.processor.BeanListProcessor
import com.univocity.parsers.csv.CsvParserSettings
import com.univocity.parsers.csv.CsvRoutines
import java.io.InputStreamReader

class Shops(context: Context) {

    private val shops: List<Shop>

    init {
        val reader = InputStreamReader(context.resources.assets.open("suica/shops.csv"))
        val routines = CsvRoutines(CsvParserSettings().also {
            it.setProcessor(BeanListProcessor(Shop::class.java))
        })
        shops = routines.parseAll(Shop::class.java, reader)
    }

    fun get(areaCode: Int, terminalCode: Int, lineCode: Int, stationCode: Int): Shop? =
        shops.find { it.areaCode == areaCode && it.terminalCode == terminalCode && it.lineCode == lineCode && it.stationCode == stationCode }

}

data class Shop(
    @Parsed(field = ["area_code"])
    val areaCode: Int = 0,
    @Parsed(field = ["terminal_code"])
    val terminalCode: Int = 0,
    @Parsed(field = ["line_code"])
    val lineCode: Int = 0,
    @Parsed(field = ["station_code"])
    val stationCode: Int = 0,
    @Parsed(field = ["company_name"], defaultNullRead = "")
    val companyName: String = "",
    @Parsed(field = ["shopName"], defaultNullRead = "")
    val shopName: String = "",
    @Parsed(field = ["cashier"], defaultNullRead = "")
    val cashier: String = "",
    @Parsed(field = ["note"], defaultNullRead = "")
    val note: String = ""
)