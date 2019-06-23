package work.deka.nfc.command.impl

import android.nfc.tech.NfcF
import work.deka.nfc.command.NfcFCommand
import java.io.ByteArrayOutputStream

// http://wiki.onakasuita.org/pukiwiki/?FeliCa%2F%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%2FRead%20Without%20Encryption
class ReadWithoutEncryption(
    nfc: NfcF,
    private val idm: ByteArray,
    private val serviceCode: ByteArray,
    private val size: Int
) : NfcFCommand<ReadWithoutEncryption.Response>(nfc) {

    override val commandCode: Byte = 0x06.toByte()
    override val responseCode: Byte = 0x07.toByte()
    override fun parse(data: ByteArray): Response = Response(data)
    override fun command(): ByteArray = ByteArrayOutputStream().also {
        it.write(commandCode.toInt())           // コマンドコード (Read Without Encryption)
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

    class Response(data: ByteArray) : NfcFCommand.Response(data)

}