package work.deka.nfc.util

fun hex(bytes: ByteArray, separator: CharSequence = " "): String = bytes.joinToString(separator) { hex(it) }
fun hex(byte: Byte): String = "%02x".format(byte)