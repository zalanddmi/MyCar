package com.example.mycar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.R
import com.example.mycar.entities.Expense

class HistoryItemAdapter(private val expenses: List<Expense>, private val listener: OnItemClickListener) : RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTypeItem: TextView = itemView.findViewById(R.id.textViewTypeItem)
        val textViewDataHistoryItem: TextView = itemView.findViewById(R.id.textViewDataHistoryItem)
        val textViewMileageHistoryItem: TextView = itemView.findViewById(R.id.textViewMileageHistoryItem)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemAdapter.ViewHolder, position: Int) {
        val expense = expenses[position]
        holder.textViewTypeItem.text = expense.type
        var textData: String
        if (expense.isService) {
            textData = "${expense.date} / ${expense.sum} рублей"
        }
        else {
            textData = "${expense.date} / ${expense.sum} рублей / ${expense.volume} л"
        }
        holder.textViewDataHistoryItem.text = textData
        holder.textViewMileageHistoryItem.text = "${expense.mileage} км"
    }

    override fun getItemCount(): Int {
        return expenses.size
    }
}