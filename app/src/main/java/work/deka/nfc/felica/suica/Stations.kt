package work.deka.nfc.felica.suica

import android.content.Context
import com.univocity.parsers.annotations.Parsed
import com.univocity.parsers.common.processor.BeanListProcessor
import com.univocity.parsers.csv.CsvParserSettings
import com.univocity.parsers.csv.CsvRoutines
import java.io.InputStreamReader

class Stations(context: Context) {

    private val stations: List<Station>

    init {
        val reader = InputStreamReader(context.resources.assets.open("suica/stations.csv"))
        val routines = CsvRoutines(CsvParserSettings().also {
            it.setProcessor(BeanListProcessor(Station::class.java))
        })
        stations = routines.parseAll(Station::class.java, reader)
    }

    fun get(areaCode: Int, lineCode: Int, stationCode: Int): Station? =
        stations.find { it.areaCode == areaCode && it.lineCode == lineCode && it.stationCode == stationCode }

}

data class Station(
    @Parsed(field = ["area_code"])
    val areaCode: Int = 0,
    @Parsed(field = ["line_code"])
    val lineCode: Int = 0,
    @Parsed(field = ["station_code"])
    val stationCode: Int = 0,
    @Parsed(field = ["company_name"], defaultNullRead = "")
    val companyName: String = "",
    @Parsed(field = ["line_name"], defaultNullRead = "")
    val lineName: String = "",
    @Parsed(field = ["station_name"], defaultNullRead = "")
    val stationName: String = "",
    @Parsed(field = ["note"], defaultNullRead = "")
    val note: String = ""
)