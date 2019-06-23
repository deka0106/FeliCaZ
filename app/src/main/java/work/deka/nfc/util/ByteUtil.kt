package work.deka.nfc.util

fun hex(bytes: ByteArray, separator: CharSequence = " "): String = bytes.joinToString(separator) { hex(it) }
fun hex(byte: Byte): String = "%02x".format(byte)
fun combine(bytes: ByteArray): Int = bytes.map(Byte::toInt).reduce { acc, byte -> (acc shl 8) + byte }