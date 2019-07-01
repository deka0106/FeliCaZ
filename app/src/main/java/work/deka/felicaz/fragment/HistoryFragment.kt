package work.deka.felicaz.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_history.*
import work.deka.felicaz.R
import work.deka.felicaz.history.History
import work.deka.felicaz.history.HistoryAdapter
import work.deka.felicaz.util.zaim
import work.deka.zaim.home.Mode

class HistoryFragment : Fragment() {

    private val historyAdapter = HistoryAdapter(ArrayList())

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entries_view.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        }
        updateRegisterButton()
    }

    fun add(history: History) {
        historyAdapter.add(history)
        updateRegisterButton()
    }

    fun addAll(histories: List<History>) {
        historyAdapter.addAll(histories)
        updateRegisterButton()
    }

    fun register(context: Context) {
        val zaim = zaim(context)
        val account = zaim.getAccount().execute().accounts.firstOrNull()?.id ?: 0L
        historyAdapter.histories.forEach {
            if (it.checked) {
                when (it.mode) {
                    Mode.PAYMENT -> {
                        Log.d("PAYMENT", zaim.postMoneyPayment(1, it.categoryId, it.genreId, it.amount, it.date).apply {
                            name = it.name
                            comment = it.comment
                            fromAccountId = account
                        }.execute().toString())
                        it.checked = false
                    }
                    Mode.INCOME -> {
                        Log.d("INCOME", zaim.postMoneyIncome(1, it.categoryId, it.amount, it.date).apply {
                            comment = it.comment
                            toAccountId = account
                        }.execute().toString())
                        it.checked = false
                    }
                    else -> {
                    }
                }
            }
        }
        handler.post {
            historyAdapter.notifyDataSetChanged()
            updateRegisterButton()
        }
    }

    private fun updateRegisterButton() {
        register.isEnabled = historyAdapter.histories.any { it.checked }
    }

}