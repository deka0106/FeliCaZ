package work.deka.nfc.command.impl

import android.nfc.tech.NfcF
import work.deka.nfc.command.NfcFCommand
import work.deka.nfc.util.int
import java.io.ByteArrayOutputStream

// http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%2FPolling
class Polling(
    override val nfc: NfcF,
    private val systemCode: ByteArray
) : NfcFCommand<Polling.Response> {

    override val commandCode = 0x00
    override val responseCode = 0x01
    override fun parse(data: ByteArray): Response = Response(data)
    override fun command(): ByteArray = ByteArrayOutputStream().also {
        it.write(commandCode)   // コマンドコード (Polling)
        it.write(systemCode)            // システムコード
        it.write(0x01)                  // リクエストコード
        it.write(0x0f)                  // タイムスロット
    }.toByteArray()

    class Response(override val data: ByteArray) : NfcFCommand.Response {
        val size by lazy { int(data[0]) }
        val responseCode by lazy { int(data[1]) }
        val idm by lazy { data.sliceArray(2 until 10) }
        val pmm by lazy { data.sliceArray(10 until 18) }
        val requestData by lazy { data.sliceArray(18 until 20) }
    }

}
