package work.deka.nfc

import android.nfc.Tag
import android.nfc.tech.NfcF
import android.util.Log
import work.deka.nfc.command.impl.Polling
import work.deka.nfc.command.impl.ReadWithoutEncryption
import work.deka.nfc.exception.NfcException

class NfcFReader(tag: Tag) {

    val nfc by lazy { NfcF.get(tag) ?: throw NfcException("Failed to get $tag.") }

    fun read(size: Int): ByteArray {
        try {
            nfc.connect()

            val pollingResponse = polling(nfc.systemCode).execute()

            Log.d(TAG, pollingResponse.data.joinToString(" ") { "%02x".format(it) })
            // http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B5%E3%83%BC%E3%83%93%E3%82%B9%E3%82%B3%E3%83%BC%E3%83%89
            val serviceCode = byteArrayOf(0x09.toByte(), 0x0f.toByte()) // Suica利用履歴
            val readWithoutEncryptionResponse = readWithoutEncryption(pollingResponse.idm, serviceCode, size).execute()

            nfc.close()

            if (readWithoutEncryptionResponse.data[10].toInt() != 0x00) throw NfcException("Failed by ${readWithoutEncryptionResponse.data[10]}.")
            return readWithoutEncryptionResponse.data
        } catch (e: Exception) {
            throw NfcException("Failed to read NFC-F.", e)
        }
    }

    private fun polling(systemCode: ByteArray): Polling = Polling(nfc, systemCode)

    private fun readWithoutEncryption(idm: ByteArray, serviceCode: ByteArray, size: Int): ReadWithoutEncryption =
        ReadWithoutEncryption(nfc, idm, serviceCode, size)

    companion object {
        val TAG = NfcFReader::class.simpleName ?: "NFC-F"
    }

}