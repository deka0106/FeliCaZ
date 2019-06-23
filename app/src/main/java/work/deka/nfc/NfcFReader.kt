package work.deka.nfc

import android.nfc.Tag
import android.nfc.tech.NfcF
import work.deka.nfc.exception.NfcException
import java.io.ByteArrayOutputStream
import java.util.*

class NfcFReader {

    fun read(tag: Tag): ByteArray {
        val nfc = NfcF.get(tag)
        try {
            nfc.connect()

            val polling = polling(nfc.systemCode)
            val pollingResponse = nfc.transceive(polling)
            val idm = Arrays.copyOfRange(pollingResponse, 2, 10)

            // http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B5%E3%83%BC%E3%83%93%E3%82%B9%E3%82%B3%E3%83%BC%E3%83%89
            val size = 4
            val serviceCode = byteArrayOf(0x09.toByte(), 0x0f.toByte()) // Suica利用履歴

            val readWithoutEncryption = readWithoutEncryption(idm, size, serviceCode)
            val readWithoutEncryptionResponse = nfc.transceive(readWithoutEncryption)

            nfc.close()

            if (readWithoutEncryptionResponse[10].toInt() != 0x00) throw NfcException("Failed by ${readWithoutEncryptionResponse[10]}.")
            return readWithoutEncryptionResponse
        } catch (e: Exception) {
            throw NfcException("Failed to read NfcF.", e)
        }
    }

    private fun polling(systemCode: ByteArray): ByteArray {
        // http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%2FPolling
        val out = ByteArrayOutputStream()
        out.write(0x00)         // データ長 (ダミー)
        out.write(0x00)         // コマンドコード (Polling)
        out.write(systemCode)   // システムコード
        out.write(0x01)         // リクエストコード
        out.write(0x0f)         // タイムスロット

        val request = out.toByteArray()
        request[0] = request.size.toByte()
        return request
    }

    private fun readWithoutEncryption(idm: ByteArray, size: Int, serviceCode: ByteArray): ByteArray {
        // http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%2FRead%20Without%20Encryption
        val out = ByteArrayOutputStream()
        out.write(0)                            // データ長 (ダミー)
        out.write(0x06)                         // コマンドコード (Read Without Encryption)
        out.write(idm)                          // IDm
        out.write(1)                            // サービス数
        out.write(serviceCode.reversedArray())  // サービスコード
        out.write(size)                         // ブロック数
        for (i in 0 until size) {               // ブロックリスト
            // http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%83%96%E3%83%AD%E3%83%83%E3%82%AF%E3%83%AA%E3%82%B9%E3%83%88
            out.write(0b10000000)               // 長さ (1bit) + アクセスモード (2bit) + サービスコードリスト順番 (4bit)
            out.write(i)                        // ブロック番号
        }

        val request = out.toByteArray()
        request[0] = request.size.toByte()
        return request
    }

    companion object {
        val TAG = NfcFReader::class.simpleName ?: "NfcF"
    }

}