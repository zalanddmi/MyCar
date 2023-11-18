package com.example.mycar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.entities.Car

class CarItemAdapter(private val cars: List<Car>, private val listener: OnItemClickListener) : RecyclerView.Adapter<CarItemAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textviewMarkItem: TextView = itemView.findViewById(R.id.textviewMarkItem)
        val textviewModelItem: TextView = itemView.findViewById(R.id.textviewModelItem)
        val textviewMileageItem: TextView = itemView.findViewById(R.id.textviewMileageItem)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = cars[position]
        holder.textviewMarkItem.text = car.mark
        holder.textviewModelItem.text = car.model
        holder.textviewMileageItem.text = "Пробег: ${car.mileage} км"
    }

    override fun getItemCount(): Int {
        return cars.size
    }
}