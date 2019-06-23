package work.deka.nfc.command

import android.nfc.tech.NfcF
import work.deka.nfc.exception.NfcException
import java.io.ByteArrayOutputStream

abstract class Request<T : Response>(val nfc: NfcF) {
    protected abstract fun parse(data: ByteArray): T
    protected abstract fun command(): ByteArray

    fun execute(): T {
        try {
            val out = ByteArrayOutputStream()
            val command = command()
            out.write(command.size)
            out.write(command)
            if (!nfc.isConnected) nfc.connect()
            val data = nfc.transceive(out.toByteArray())
            nfc.close()
            return parse(data)
        } catch (e: Exception) {
            throw NfcException("Failed to execute NFC-F command.", e)
        }
    }
}