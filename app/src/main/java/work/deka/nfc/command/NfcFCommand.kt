package work.deka.nfc.command

import android.nfc.tech.NfcF
import android.util.Log
import work.deka.nfc.exception.NfcException
import work.deka.nfc.util.hex
import java.io.ByteArrayOutputStream

interface NfcFCommand<T : NfcFCommand.Response> {

    val nfc: NfcF

    val commandCode: Int
    val responseCode: Int

    fun parse(data: ByteArray): T
    fun command(): ByteArray

    fun execute(): T {
        try {
            val out = ByteArrayOutputStream()
            val command = command()
            out.write(command.size + 1)
            out.write(command)
            if (!nfc.isConnected) nfc.connect()
            Log.d(TAG, hex(out.toByteArray()))
            return parse(nfc.transceive(out.toByteArray()))
        } catch (e: Exception) {
            throw NfcException("Failed to execute NFC-F command.", e)
        }
    }

    interface Response {
        val data: ByteArray
    }

    companion object {
        val TAG = NfcFCommand::class.simpleName ?: "NfcFCommand"
    }

}
