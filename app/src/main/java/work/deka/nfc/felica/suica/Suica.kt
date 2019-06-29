package work.deka.nfc.felica.suica

import android.util.SparseArray
import work.deka.nfc.util.combine
import work.deka.nfc.util.int
import java.util.*

// https://ja.osdn.net/projects/felicalib/wiki/suica
// http://jennychan.web.fc2.com/format/suica.html
class Suica {

    class Entry(val data: ByteArray) {

        val terminalCode by lazy { int(data[0]) }
        val terminal by lazy { TERMINAL[terminalCode] }

        val processCode by lazy { int(data[1]) }
        val process by lazy { PROCESS[processCode] }

        val paymentCode by lazy { int(data[2]) }
        val payment by lazy { PAYMENT[paymentCode] }

        val accessCode by lazy { int(data[3]) }
        val access by lazy { ACCESS[accessCode] }

        val date: Date by lazy {
            val (year, month, day) = combine(data.sliceArray(4 until 6)).let {
                Triple(
                    it and 0b1111111000000000 shr 9,
                    it and 0b0000000111100000 shr 5,
                    it and 0b0000000000011111
                )
            }
            val (hour, minute, second) = when {
                isShopping -> combine(data.sliceArray(6 until 8)).let {
                    Triple(
                        it and 0b1111100000000000 shr 11,
                        it and 0b0000011111100000 shr 5,
                        it and 0b0000000000011111
                    )
                }
                else -> Triple(0, 0, 0)
            }
            Calendar.getInstance().apply { set(2000 + year, month, day, hour, minute, second) }.time
        }

        val inAreaCode by lazy { int(data[15]) and 0b11000000 shr 6 }
        val inLineCode by lazy { int(data[6]) }
        val inStationCode by lazy { int(data[7]) }

        val outAreaCode by lazy { int(data[15]) and 0b00110000 shr 4 }
        val outLineCode by lazy { int(data[8]) }
        val outStationCode by lazy { int(data[9]) }

        val balance by lazy { combine(data.sliceArray(10 until 12).reversedArray()) }
        val serial by lazy { combine(data.sliceArray(13 until 15)) }
        val region by lazy { int(data[15]) }

        val isShopping by lazy { isShopping(processCode) }
        val isBus by lazy { isBus(processCode) }

        val isStation by lazy { isStation(terminalCode) }
        val isShop by lazy { isShop(terminalCode) }
        val isBusStation by lazy { isBusStation(terminalCode) }

        override fun toString(): String = mapOf(
            "端末種" to terminal,
            "処理" to process,
            "支払" to payment,
            "入出場" to access,
            "日付" to date,
            "残高" to balance
        ).toString()

    }

    companion object {
        val SERVICE_CODE = byteArrayOf(0x09.toByte(), 0x0f.toByte()) // 利用履歴

        val TERMINAL = SparseArray<String>().apply {
            put(0x03, "精算機")
            put(0x04, "携帯型端末")
            put(0x05, "車載端末")
            put(0x07, "券売機")
            put(0x08, "券売機")
            put(0x09, "入金機")
            put(0x12, "券売機")
            put(0x14, "券売機等")
            put(0x15, "券売機等")
            put(0x16, "改札機")
            put(0x17, "簡易改札機")
            put(0x18, "窓口端末")
            put(0x19, "窓口端末")
            put(0x1A, "改札端末")
            put(0x1B, "携帯電話")
            put(0x1C, "乗継精算機")
            put(0x1D, "連絡改札機")
            put(0x1F, "簡易入金機")
            put(0x46, "VIEW ALTTE")
            put(0x48, "VIEW ALTTE")
            put(0xC7, "物販端末")
            put(0xC8, "自販機")
        }

        val PROCESS = SparseArray<String>().apply {
            put(0x01, "改札出場")
            put(0x02, "チャージ")
            put(0x03, "磁気券購入")
            put(0x04, "精算")
            put(0x05, "入場精算")
            put(0x06, "改札窓口処理")
            put(0x07, "新規発行")
            put(0x08, "窓口控除")
            put(0x0D, "バス (PiTaPa系)")
            put(0x0F, "バス (IruCa系)")
            put(0x11, "再発行処理")
            put(0x13, "支払 (新幹線利用)")
            put(0x14, "入場時オートチャージ")
            put(0x15, "出場時オートチャージ")
            put(0x1F, "バスチャージ")
            put(0x23, "バス路面電車企画券購入")
            put(0x46, "物販")
            put(0x48, "特典チャージ")
            put(0x49, "レジ入金")
            put(0x4A, "物販取消")
            put(0x4B, "入場物販")
            put(0xC6, "現金併用物販")
            put(0xCB, "入場現金併用物販")
            put(0x84, "他社精算")
            put(0x85, "他社入場精算")
        }

        val PAYMENT = SparseArray<String>().apply {
            put(0x00, "通常")
            put(0x02, "VIEW")
            put(0x0B, "PiTaPa")
            put(0x0D, "PASMO (オートチャージ)")
            put(0x3F, "モバイルSuica")
        }

        val ACCESS = SparseArray<String>().apply {
            put(0x00, "通常出場")
            put(0x01, "入場 (オートチャージ)")
            put(0x02, "入場+出場")
            put(0x03, "定期入場+出場")
            put(0x04, "入場+定期出場")
            put(0x05, "乗継割引")
            put(0x0E, "窓口出場")
            put(0x0F, "バス/路面等")
            put(0x17, "乗継割引 (バス→鉄道)")
            put(0x1D, "乗継割引 (バス)")
            put(0x21, "乗継精算")
        }

        fun isShopping(processCode: Int): Boolean = when (processCode) {
            0x46, 0x49, 0x4A, 0x4B, 0xC6, 0xCB -> true
            else -> false
        }

        fun isBus(processCode: Int): Boolean = when (processCode) {
            0x0D, 0x0F, 0x1F, 0x23 -> true
            else -> false
        }

        fun isStation(terminalCode: Int): Boolean = when (terminalCode) {
            0xC7, 0xC8, 0x05 -> false
            else -> true
        }

        fun isShop(terminalCode: Int): Boolean = when (terminalCode) {
            0xC7, 0xC8 -> true
            else -> false
        }

        fun isBusStation(terminalCode: Int): Boolean = when (terminalCode) {
            0x05 -> true
            else -> false
        }

    }
}