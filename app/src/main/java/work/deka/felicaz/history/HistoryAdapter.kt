package work.deka.felicaz.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import work.deka.felicaz.R
import work.deka.felicaz.fragment.HistoryFragment
import work.deka.zaim.home.Mode
import work.deka.zaim.util.format

class HistoryAdapter(private val parent: HistoryFragment) :
    RecyclerView.Adapter<HistoryViewHolder>() {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = parent.histories[position]
        val amount = "${if (history.mode == Mode.INCOME) "+" else "-"} ${history.amount}"
        holder.name.text = history.name
        holder.comment.text = history.comment
        holder.amount.text = amount
        holder.date.text = format(history.date)
        holder.check.isChecked = history.checked
        holder.check.setOnCheckedChangeListener { _, isChecked ->
            history.checked = isChecked
            parent.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = parent.histories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder =
        HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.history, parent, false))

}