package work.deka.felicaz

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import work.deka.felicaz.fragment.HistoryFragment
import work.deka.felicaz.fragment.HomeFragment
import work.deka.felicaz.fragment.SettingFragment
import work.deka.felicaz.util.saveCredentials
import work.deka.felicaz.util.zaim
import work.deka.nfc.NfcFReader
import work.deka.nfc.exception.NfcException
import work.deka.nfc.felica.suica.Stations
import work.deka.zaim.exception.ZaimException
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val nfcAdapter by lazy { NfcAdapter.getDefaultAdapter(this) }
    private val nfcPendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
    }
    private val nfcFilter by lazy { IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { addDataType("*/*") } }

    private val zaim by lazy { zaim(applicationContext) }

    private val job by lazy { Job() }
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private val fragments by lazy {
        listOf(HomeFragment(), HistoryFragment(), SettingFragment())
    }
    private val pagerAdapter by lazy {
        object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount() = fragments.size
            override fun getItem(position: Int) = fragments[position]
        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                view_pager.setCurrentItem(0, true)
                title = getString(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                view_pager.setCurrentItem(1, true)
                title = getString(R.string.title_history)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                view_pager.setCurrentItem(2, true)
                title = getString(R.string.title_setting)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.title_home)
        view_pager.also {
            it.adapter = pagerAdapter
            it.offscreenPageLimit = fragments.size - 1
        }
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
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

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        job.cancel()
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "onNewIntent")
        super.onNewIntent(intent)
        if (intent == null) return
        when (intent.action) {
            NfcAdapter.ACTION_TECH_DISCOVERED -> onTechDiscovered(intent)
            Intent.ACTION_VIEW -> onActionView(intent)
        }
    }

    private fun onTechDiscovered(intent: Intent) {
        Log.d(TAG, "onTechDiscovered")

        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        Log.d(TAG, tag.toString())

        val nfcReader = NfcFReader(tag)
        try {
            val stations = Stations(applicationContext)
            nfcReader.read(15).forEach {
                Log.d(TAG, it.toString())
                Log.d(
                    TAG,
                    "${
                    stations.get(it.inAreaCode, it.inLineCode, it.inStationCode)?.stationName
                    } (${
                    "${it.inAreaCode} ${it.inLineCode} ${it.inStationCode}"
                    }) -> ${
                    stations.get(it.outAreaCode, it.outLineCode, it.outStationCode)?.stationName
                    } (${
                    "${it.outAreaCode} ${it.outLineCode} ${it.outStationCode}"
                    })"
                )
            }
        } catch (e: NfcException) {
            e.printStackTrace()
        }
    }

    private fun onActionView(intent: Intent) {
        Log.d(TAG, "onActionView")
        val uri = intent.data ?: return
        val code = uri.getQueryParameter(OAUTH_VERIFIER) ?: return
        launch {
            withContext(Dispatchers.IO) {
                try {
                    zaim.authorize(code)
                    saveCredentials(applicationContext, zaim.credentials)
                    Log.d(TAG, zaim.getUserVerify().execute().toString())
                } catch (e: ZaimException) {
                    Toast.makeText(applicationContext, getString(R.string.failed_to_authorize), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun startAuthorize(view: View) {
        launch {
            Intent(Intent.ACTION_VIEW).let {
                it.data = Uri.parse(withContext(Dispatchers.IO) { zaim.getAuthorizeUrl() })
                view.context.startActivity(it)
            }
        }
    }

    companion object {
        val TAG = MainActivity::class.simpleName ?: "Main"
        val NFC_TYPES = arrayOf(NfcF::class.java.name)
        const val OAUTH_VERIFIER = "oauth_verifier"
    }

}
