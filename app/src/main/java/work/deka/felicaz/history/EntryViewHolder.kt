package work.deka.felicaz.history

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import work.deka.felicaz.R

class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameView: TextView = view.findViewById(R.id.entry_name)
    val checkBox: CheckBox = view.findViewById(R.id.entry_check)
}