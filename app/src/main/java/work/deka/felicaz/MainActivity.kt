package work.deka.felicaz

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val nfcAdapter by lazy { NfcAdapter.getDefaultAdapter(this) }
    private val nfcPendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
    }
    private val nfcFilter by lazy { IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { addDataType("*/*") } }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, arrayOf(nfcFilter), arrayOf(NFC_TYPES))
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "onNewIntent")
        super.onNewIntent(intent)
        if (intent == null) return
        when (intent.action) {
            NfcAdapter.ACTION_TECH_DISCOVERED -> onTechDiscovered(intent)
        }
    }

    private fun onTechDiscovered(intent: Intent) {
        Log.d(TAG, "onTechDiscovered")

        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        Log.d(TAG, tag.toString())
    }

    companion object {
        val TAG = MainActivity::class.simpleName ?: "Main"
        val NFC_TYPES = arrayOf(NfcF::class.java.name)
    }

}
