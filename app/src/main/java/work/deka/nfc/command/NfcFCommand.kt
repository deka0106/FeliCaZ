package work.deka.nfc.command

import android.nfc.tech.NfcF
import android.util.Log
import work.deka.nfc.exception.NfcException
import java.io.ByteArrayOutputStream

abstract class NfcFCommand<T : NfcFCommand.Response>(private val nfc: NfcF) {

    abstract val commandCode: Byte
    abstract val responseCode: Byte

    protected abstract fun parse(data: ByteArray): T
    protected abstract fun command(): ByteArray

    fun execute(): T {
        try {
            val out = ByteArrayOutputStream()
            val command = command()
            out.write(command.size + 1)
            out.write(command)
            if (!nfc.isConnected) nfc.connect()
            Log.d(TAG, out.toByteArray().joinToString(" ") { "%02x".format(it) })
            return parse(nfc.transceive(out.toByteArray()))
        } catch (e: Exception) {
            throw NfcException("Failed to execute NFC-F command.", e)
        }
    }

    abstract class Response(val data: ByteArray)

    companion object {
        val TAG = NfcFCommand::class.simpleName ?: "NfcFCommand"
    }

}
