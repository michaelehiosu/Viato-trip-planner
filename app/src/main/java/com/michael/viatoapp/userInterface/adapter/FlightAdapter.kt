package com.michael.viatoapp.userInterface.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michael.viatoapp.model.response.Flight
import com.michael.viatoapp.R

class FlightAdapter(private val flightList: MutableList<Flight>) : RecyclerView.Adapter<FlightAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val depAirport: TextView = itemView.findViewById(R.id.dep_airport)
        val arrAirport: TextView = itemView.findViewById(R.id.arr_airport)
        val price: TextView = itemView.findViewById(R.id.price)
        val flightLength: TextView = itemView.findViewById(R.id.flight_lenght)
        val layovers: TextView = itemView.findViewById(R.id.layovers)
        val depDate: TextView = itemView.findViewById(R.id.dep_date)
        val arrDate: TextView = itemView.findViewById(R.id.arr_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.flight_recycler, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = flightList[position]
        holder.depAirport.text = currentItem.depAirport
        holder.arrAirport.text = currentItem.arrAirport
        holder.price.text = currentItem.price
        holder.flightLength.text = currentItem.flightLength
        holder.layovers.text = currentItem.layovers
        holder.depDate.text = currentItem.depDate
        holder.arrDate.text = currentItem.arrDate
    }

    override fun getItemCount() : Int {
        return flightList.size
    }
}