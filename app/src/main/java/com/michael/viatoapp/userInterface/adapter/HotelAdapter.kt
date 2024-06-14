package com.michael.viatoapp.userInterface.adapter

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michael.viatoapp.R
import java.io.IOException
import java.util.Locale

class HotelAdapter(private val hotelList: MutableList<com.michael.viatoapp.model.data.stays.Hotel>) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotel_name)
        val address: TextView = itemView.findViewById(R.id.address)
        val gradeNumber: TextView = itemView.findViewById(R.id.grade_number)
        val gradeWord: TextView = itemView.findViewById(R.id.grade_word)
        val price: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hotel_recycler, parent, false)
        return HotelViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val currentItem = hotelList[position]
        val name = currentItem.name ?: ""
        holder.hotelName.text = truncateString(currentItem.name, 25)
        val reviewScore = currentItem.reviewScore ?: 0.0
        holder.gradeNumber.text = reviewScore.toString()

        val gradeWord = when (reviewScore) {
            in 0.0..2.0 -> "Bad"
            in 2.1..3.0 -> "Ok"
            in 3.1..4.0 -> "Good"
            in 4.1..5.0 -> "Excellent"
            else -> "Unknown"
        }
        holder.gradeWord.text = gradeWord

        holder.price.text = "€ " + currentItem.priceRaw.toString()

        val latitude = currentItem.latitude
        val longitude = currentItem.longitude

        val geocoder = Geocoder(holder.itemView.context, Locale.getDefault())
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.let {
                if (it.isNotEmpty()) {
                    val address: Address = it[0]
                    val truncatedAddress = truncateString(address.getAddressLine(0), 40)
                    holder.address.text = truncatedAddress
                } else {
                    holder.address.text = "Address not found"
                }
            } ?: run {
                holder.address.text = "Geocoding failed"
            }
        } catch (e: IOException) {
            holder.address.text = "Geocoding failed"
            e.printStackTrace()
        }
    }

    fun truncateString(input: String?, maxLength: Int): String {
        if (input == null) return ""
        return if (input.length > maxLength) {
            input.substring(0, maxLength - 3) + "..."
        } else {
            input
        }
    }

    override fun getItemCount() = hotelList.size
}
