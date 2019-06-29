package work.deka.nfc.util

fun hex(bytes: ByteArray, separator: CharSequence = " "): String = bytes.joinToString(separator) { hex(it) }
fun hex(byte: Byte): String = "%02x".format(byte)
fun hex(int: Int): String = "%02x".format(int)
fun int(byte: Byte): Int = byte.toInt() and 0xff
fun combine(bytes: ByteArray): Int = bytes.map(::int).reduce { acc, byte -> (acc shl 8) + byte }