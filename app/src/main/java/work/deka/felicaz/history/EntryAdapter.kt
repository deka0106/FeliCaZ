package work.deka.felicaz.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import work.deka.felicaz.R

class EntryAdapter(private val entries: MutableList<Entry>) :
    RecyclerView.Adapter<EntryViewHolder>() {

    private var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.nameView.text = entries[position].name
        holder.checkBox.isChecked = true
    }

    override fun getItemCount(): Int = entries.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mView = layoutInflater.inflate(R.layout.entry, parent, false)
        return EntryViewHolder(mView)
    }

    fun addEntry(entry: Entry) {
        entries.add(entry)
        notifyDataSetChanged()
    }

    fun addEntries(entries: List<Entry>) {
        this.entries.addAll(entries)
        notifyDataSetChanged()
    }

}