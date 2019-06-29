package work.deka.nfc.command.impl

import android.nfc.tech.NfcF
import work.deka.nfc.command.NfcFCommand
import work.deka.nfc.util.int
import java.io.ByteArrayOutputStream

// http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%2FRead%20Without%20Encryption
class ReadWithoutEncryption(
    override val nfc: NfcF,
    private val idm: ByteArray,
    private val serviceCode: ByteArray,
    private val size: Int
) : NfcFCommand<ReadWithoutEncryption.Response> {

    override val commandCode = 0x06
    override val responseCode = 0x07
    override fun parse(data: ByteArray): Response = Response(data)
    override fun command(): ByteArray = ByteArrayOutputStream().also {
        it.write(commandCode)                   // コマンドコード (Read Without Encryption)
        it.write(idm)                           // IDm
        it.write(1)                             // サービス数
        it.write(serviceCode.reversedArray())   // サービスコード
        it.write(size)                          // ブロック数
        for (i in 0 until size) {               // ブロックリスト
            // http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%83%96%E3%83%AD%E3%83%83%E3%82%AF%E3%83%AA%E3%82%B9%E3%83%88
            it.write(0b10000000)                // 長さ (1bit) + アクセスモード (2bit) + サービスコードリスト順番 (4bit)
            it.write(i)                         // ブロック番号
        }
    }.toByteArray()

    class Response(override val data: ByteArray) : NfcFCommand.Response {
        val size by lazy { int(data[0]) }
        val responseCode by lazy { int(data[1]) }
        val idm by lazy { data.sliceArray(2 until 10) }
        val status1 by lazy { int(data[10]) }
        val status2 by lazy { int(data[11]) }
        val blockCount by lazy { int(data[12]) }
        val blocks by lazy { Array(blockCount) { data.sliceArray(13 + it * 16 until 13 + (it + 1) * 16) } }
        val ok by lazy { status1 == 0x00 && status2 == 0x00 }
    }

}