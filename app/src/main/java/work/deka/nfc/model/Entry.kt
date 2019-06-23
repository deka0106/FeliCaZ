package work.deka.nfc.model

import android.util.SparseArray
import work.deka.nfc.util.combine
import java.util.*

// https://ja.osdn.net/projects/felicalib/wiki/suica
class Entry(val data: ByteArray) {

    val terminalCode by lazy { data[0].toInt() }
    val terminal by lazy { TERMINAL[terminalCode] }

    val processCode by lazy { data[1].toInt() }
    val process by lazy { PROCESS[processCode] }

    val date: Date by lazy {
        val combine = combine(data.sliceArray(4 until 6))
        val year = combine and 0b1111111000000000 shr 9
        val month = combine and 0b0000000111100000 shr 5
        val day = combine and 0b0000000000011111
        Calendar.getInstance().apply { set(2000 + year, month, day) }.time
    }

    val inLineCode by lazy { data[6].toInt() }
    val inStationCode by lazy { data[7].toInt() }
    val outLineCode by lazy { data[8].toInt() }
    val outStationCode by lazy { data[9].toInt() }

    val balance by lazy { combine(data.sliceArray(10 until 12).reversedArray()) }
    val serial by lazy { data.sliceArray(12 until 15) }
    val reasion by lazy { data[15].toInt() }

    val type: String by lazy {
        when {
            isShopping(processCode) -> "物販"
            isBus(processCode) -> "バス"
            inLineCode < 0x80 -> "JR"
            reasion == 0x00 -> "関東公営・私鉄"
            reasion == 0x01 -> "関西公営・私鉄"
            else -> "その他"
        }
    }

    override fun toString(): String = mapOf(
        "端末種" to terminal,
        "処理" to process,
        "日付" to date,
        "残高" to balance,
        "種類" to type
    ).toString()

    companion object {

        val TERMINAL = SparseArray<String>().apply {
            put(3, "精算機")
            put(4, "携帯型端末")
            put(5, "車載端末")
            put(7, "券売機")
            put(8, "券売機")
            put(9, "入金機")
            put(18, "券売機")
            put(20, "券売機等")
            put(21, "券売機等")
            put(22, "改札機")
            put(23, "簡易改札機")
            put(24, "窓口端末")
            put(25, "窓口端末")
            put(26, "改札端末")
            put(27, "携帯電話")
            put(28, "乗継精算機")
            put(29, "連絡改札機")
            put(31, "簡易入金機")
            put(70, "VIEW ALTTE")
            put(72, "VIEW ALTTE")
            put(199, "物販端末")
            put(200, "自販機")
        }

        val PROCESS = SparseArray<String>().apply {
            put(1, "運賃支払 (改札出場)")
            put(2, "チャージ")
            put(3, "券購 (磁気券購入)")
            put(4, "精算")
            put(5, "精算 (入場精算)")
            put(6, "窓出 (改札窓口処理)")
            put(7, "新規 (新規発行)")
            put(8, "控除 (窓口控除)")
            put(13, "バス (PiTaPa系)")
            put(15, "バス (IruCa系)")
            put(17, "再発 (再発行処理)")
            put(19, "支払 (新幹線利用)")
            put(20, "入A (入場時オートチャージ)")
            put(21, "出A (出場時オートチャージ)")
            put(31, "入金 (バスチャージ)")
            put(35, "券購 (バス路面電車企画券購入)")
            put(70, "物販")
            put(72, "特典 (特典チャージ)")
            put(73, "入金 (レジ入金)")
            put(74, "物販取消")
            put(75, "入物 (入場物販)")
            put(198, "物現 (現金併用物販)")
            put(203, "入物 (入場現金併用物販)")
            put(132, "精算 (他社精算)")
            put(133, "精算 (他社入場精算)")
        }

        private fun isShopping(actionId: Int): Boolean = when (actionId) {
            70, 73, 74, 75, 198, 203 -> true
            else -> false
        }

        private fun isBus(actionId: Int): Boolean = when (actionId) {
            13, 15, 31, 35 -> true
            else -> false
        }

    }
}