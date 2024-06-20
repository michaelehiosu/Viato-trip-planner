package com.michael.viatoapp.userInterface.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.michael.viatoapp.R
import com.michael.viatoapp.model.data.SearchData
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
import com.michael.viatoapp.userInterface.activities.MainNavigationActivity

class CountryAdapter(private var countries: List<Country>, private var countriesSearch: FlightCountriesSearch, private var searchData: SearchData) :
    RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nearby_activity_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = "€";
        val country = countries[position]
        holder.description.text = country.cheapestPrice?.let { "From $it$currency " } ?: "Click to see cities"
        holder.title.text = country.name
        holder.cardView.setOnClickListener {
            val context = holder.itemView.context
            if (context is MainNavigationActivity) {
                context.navigateToCountryOverviewFragment(country, countriesSearch, searchData)
            }
        }
        Glide.with(holder.itemView)
            .load(country.imageUrl) // Load image URL using Glide
            .into(holder.image)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardview_activity)
        val image: ImageView = itemView.findViewById(R.id.activity_image)
        val title: TextView = itemView.findViewById(R.id.activity_title)
        val description: TextView = itemView.findViewById(R.id.activity_description)
    }
}
