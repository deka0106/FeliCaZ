package work.deka.nfc.command.impl

import android.nfc.tech.NfcF
import work.deka.nfc.command.NfcFCommand
import java.io.ByteArrayOutputStream
import java.util.*

// http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%2FPolling
class Polling(
    nfc: NfcF,
    private val systemCode: ByteArray
) : NfcFCommand<Polling.Response>(nfc) {

    override val commandCode: Byte = 0x00.toByte()
    override val responseCode: Byte = 0x01.toByte()
    override fun parse(data: ByteArray): Response = Response(data)
    override fun command(): ByteArray = ByteArrayOutputStream().also {
        it.write(commandCode.toInt())   // コマンドコード (Polling)
        it.write(systemCode)            // システムコード
        it.write(0x01)                  // リクエストコード
        it.write(0x0f)                  // タイムスロット
    }.toByteArray()

    class Response(data: ByteArray) : NfcFCommand.Response(data) {
        val idm get() = data.sliceArray(2 until 10)
        val pmm get() = data.sliceArray(10 until 18)
    }

}
