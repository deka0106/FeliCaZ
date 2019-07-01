package work.deka.felicaz.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_history.*
import work.deka.felicaz.R
import work.deka.felicaz.history.Entry
import work.deka.felicaz.history.EntryAdapter

class HistoryFragment : Fragment() {

    private val entryAdapter = EntryAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entries_view.apply {
            adapter = entryAdapter
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun addEntry(entry: Entry) = entryAdapter.addEntry(entry)
    fun addEntries(entries: List<Entry>) = entryAdapter.addEntries(entries)

}