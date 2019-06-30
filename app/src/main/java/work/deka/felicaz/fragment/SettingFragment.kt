package work.deka.felicaz.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting.*
import work.deka.felicaz.R
import work.deka.felicaz.util.zaim

class SettingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onResume() {
        super.onResume()
        updateView()
    }

    fun updateView() {
        Log.d(TAG, "updateView")
        context?.let {
            val zaim = zaim(it)
            if (zaim.isAuthorized) {
                login.visibility = View.GONE
                logout.visibility = View.VISIBLE
            } else {
                login.visibility = View.VISIBLE
                logout.visibility = View.GONE
            }
        }
    }

    companion object {
        val TAG = SettingFragment::class.simpleName ?: "Setting"
    }

}