package work.deka.felicaz.history

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import work.deka.felicaz.R

class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.entry_name)
    val comment: TextView = view.findViewById(R.id.entry_comment)
    val amount: TextView = view.findViewById(R.id.entry_amount)
    val date: TextView = view.findViewById(R.id.entry_date)
    val check: CheckBox = view.findViewById(R.id.entry_check)
}