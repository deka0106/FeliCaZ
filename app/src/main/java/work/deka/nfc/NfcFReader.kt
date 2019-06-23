package work.deka.nfc

import android.nfc.Tag
import android.nfc.tech.NfcF
import work.deka.nfc.command.impl.Polling
import work.deka.nfc.command.impl.ReadWithoutEncryption
import work.deka.nfc.exception.NfcException
import work.deka.nfc.util.hex

class NfcFReader(private val tag: Tag) {

    val nfc by lazy { NfcF.get(tag) ?: throw NfcException("Failed to get $tag.") }

    fun read(size: Int): ByteArray {
        try {
            nfc.connect()

            // http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B5%E3%83%BC%E3%83%93%E3%82%B9%E3%82%B3%E3%83%BC%E3%83%89
            val serviceCode = byteArrayOf(0x09.toByte(), 0x0f.toByte()) // Suica利用履歴
            val response = readWithoutEncryption(tag.id, serviceCode, size).execute()

            nfc.close()

            if (!response.ok) throw NfcException("Failed by ${hex(response.status1)} or ${hex(response.status2)}.")
            return response.data
        } catch (e: Exception) {
            throw NfcException("Failed to read NFC-F.", e)
        }
    }

    fun polling(systemCode: ByteArray): Polling = Polling(nfc, systemCode)

    fun readWithoutEncryption(idm: ByteArray, serviceCode: ByteArray, size: Int): ReadWithoutEncryption =
        ReadWithoutEncryption(nfc, idm, serviceCode, size)

    companion object {
        val TAG = NfcFReader::class.simpleName ?: "NFC-F"
    }

}