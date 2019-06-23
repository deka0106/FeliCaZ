package work.deka.nfc.exception

import java.lang.RuntimeException

class NfcException : RuntimeException {
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}