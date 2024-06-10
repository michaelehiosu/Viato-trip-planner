package com.michael.viatoapp.userInterface.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.michael.viatoapp.model.response.Hotel
import com.michael.viatoapp.R
import com.michael.viatoapp.model.response.Flight

class HotelAdapter(private val hotelList: MutableList<Hotel>) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

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
        holder.hotelName.text = currentItem.hotelName
        holder.address.text = currentItem.address
        holder.gradeNumber.text = currentItem.gradeNumber
        holder.gradeWord.text = currentItem.gradeWord
        holder.price.text = currentItem.price
    }

    override fun getItemCount() = hotelList.size

    fun updateHotels(newHotels: List<Hotel>) {
        hotelList.clear()
        hotelList.addAll(newHotels)
        notifyDataSetChanged()
    }
}
