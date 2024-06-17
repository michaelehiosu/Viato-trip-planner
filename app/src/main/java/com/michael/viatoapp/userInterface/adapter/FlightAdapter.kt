package com.michael.viatoapp.userInterface.adapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.michael.viatoapp.model.response.Flight
import com.michael.viatoapp.R
import com.michael.viatoapp.model.data.flights.Itinerary
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class FlightAdapter(
    private val flightList: MutableList<Itinerary>,
    private val onItemSelected: (Itinerary?) -> Unit // Lambda function to handle selection
) : RecyclerView.Adapter<FlightAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val depAirport: TextView = itemView.findViewById(R.id.dep_airport)
        val arrAirport: TextView = itemView.findViewById(R.id.arr_airport)
        val price: TextView = itemView.findViewById(R.id.price)
        val flightLength: TextView = itemView.findViewById(R.id.flight_lenght)
        val layovers: TextView = itemView.findViewById(R.id.layovers)
        val depDate: TextView = itemView.findViewById(R.id.dep_date)
        val arrDate: TextView = itemView.findViewById(R.id.arr_date)
        val linearView: View = itemView.findViewById(R.id.ll_flights) // Ensure your layout has a CardView with this ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.flight_recycler, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
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

        // Change background color based on selection
        holder.linearView.background = (if (selectedItemPosition == position) {
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.cheapest_bg)
        } else {
            null
        })

        // Handle item click to toggle selection
        holder.itemView.setOnClickListener {
            if (selectedItemPosition == position) {
                // Deselect the current item
                selectedItemPosition = RecyclerView.NO_POSITION
                onItemSelected(null)
            } else {
                // Select a new item
                selectedItemPosition = position
                onItemSelected(currentItem)
            }
            notifyDataSetChanged() // Refresh the entire list
        }
    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    fun clearSelection() {
        selectedItemPosition = RecyclerView.NO_POSITION
    }
}
