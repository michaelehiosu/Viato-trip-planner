package com.michael.viatoapp.userInterface.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michael.viatoapp.model.response.Flight
import com.michael.viatoapp.R
import com.michael.viatoapp.model.data.flights.Itinerary
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class FlightAdapter(private val flightList: MutableList<Itinerary>) : RecyclerView.Adapter<FlightAdapter.ViewHolder>() {

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
        holder.depAirport.text = currentItem.originId
        holder.arrAirport.text = currentItem.destinationId
        holder.price.text = "€" + currentItem.rawPrice?.toInt().toString()
        holder.flightLength.text = currentItem.durationOutbound.toString() + " min"

        val stopCountOutbound = currentItem.stopCountOutbound
        holder.layovers.text = if (stopCountOutbound == 0) "Direct" else stopCountOutbound.toString()

        val outputFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val depDate = inputFormat.parse(currentItem.outboundDepartureTime)
        holder.depDate.text = outputFormat.format(depDate)
        val arrDate = inputFormat.parse(currentItem.outboundArrivalTime)
        holder.arrDate.text = outputFormat.format(arrDate)
    }

    override fun getItemCount() : Int {
        return flightList.size
    }
}