package work.deka.felicaz.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import work.deka.felicaz.R
import work.deka.zaim.home.Mode
import work.deka.zaim.util.format

class HistoryAdapter(val histories: MutableList<History>) :
    RecyclerView.Adapter<HistoryViewHolder>() {

    private var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]
        holder.name.text = history.name
        holder.comment.text = history.comment
        holder.amount.text = "${if (history.mode == Mode.INCOME) "+" else "-"} ${history.amount}"
        holder.date.text = format(history.date)
        holder.check.isChecked = history.checked
        holder.check.setOnCheckedChangeListener { _, isChecked -> history.checked = isChecked }
    }

    override fun getItemCount(): Int = histories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mView = layoutInflater.inflate(R.layout.history, parent, false)
        return HistoryViewHolder(mView)
    }

    fun add(history: History) {
        if (!histories.contains(history)) {
            histories.add(history)
            notifyDataSetChanged()
        }
    }

    fun addAll(histories: List<History>) {
        histories.forEach {
            if (!this.histories.contains(it)) this.histories.add(it)
        }
        notifyDataSetChanged()
    }

}