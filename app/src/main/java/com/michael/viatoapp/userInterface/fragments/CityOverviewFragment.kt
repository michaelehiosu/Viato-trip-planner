package com.michael.viatoapp.userInterface.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.api.ApiHelper
import com.michael.viatoapp.databinding.ActivityCityOverviewBinding
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlighCountriesSearch
import com.michael.viatoapp.model.request.stays.CitySearch
import com.michael.viatoapp.model.request.stays.HotelsSearch
import com.michael.viatoapp.userInterface.activities.MainNavigationActivity
import com.michael.viatoapp.userInterface.adapter.FlightAdapter
import com.michael.viatoapp.userInterface.adapter.HotelAdapter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CityOverviewFragment : Fragment() {
    private lateinit var binding: ActivityCityOverviewBinding
    private lateinit var flightAdapter: FlightAdapter
    private lateinit var hotelAdapter: HotelAdapter
    private var city: City? = null
    private var countrySearch : FlighCountriesSearch? = null
    private val apiClient = ApiClient()
    private val apiHelper = ApiHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            city = it.getSerializable("city") as City?
            countrySearch = it.getSerializable("countrySearch") as FlighCountriesSearch
        }
        binding = ActivityCityOverviewBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flightAdapter = FlightAdapter(mutableListOf())
        hotelAdapter = HotelAdapter(mutableListOf())

        binding.recyclerViewFlights.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFlights.adapter = flightAdapter

        binding.recyclerViewHotels.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHotels.adapter = hotelAdapter

        binding.buttonGo.setOnClickListener {
            val context = activity
            if (context is MainNavigationActivity) {
                context.navigateToMoreInfoFragment()
            }
        }

        binding.buttonGoTo.setOnClickListener {
            val context = activity
            if (context is MainNavigationActivity) {
                context.navigateToMoreInfoFragment()
            }
        }

        if(city != null && countrySearch != null) {
            fetchFlightsAndHotels()
        }

        Glide.with(this)
            .load(city?.imageUrl)
            .placeholder(R.drawable.rio_pic) // Optional placeholder
            .into(binding.cityImage)

        binding.cityTextview.text = city?.name
    }

    private fun fetchFlightsAndHotels() {
        lifecycleScope.launch {

            val flightsSearch = AllFlightsSearch(
                fromEntityId = countrySearch!!.fromEntityId,
                toEntityId = city!!.entityId,
                departDate = countrySearch!!.departDate,
                returnDate = countrySearch!!.returnDate,
                currency = countrySearch!!.currency,
                dummy = true
            )

            val citySearch = CitySearch(
                query = city!!.name,
                dummy = true
            )

            try {
                val hotelCity = apiClient.getHotelCity(citySearch)

                val hotelsSearch = HotelsSearch(
                    entityId = hotelCity.entityId,
                    checkIn = countrySearch!!.departDate,
                    checkOut = countrySearch!!.returnDate,
                    maxPrice = 200,
                    dummy = true
                )

                val flightList: MutableList<Itinerary> = apiClient.getAllFlights(flightsSearch)
                val hotelList: MutableList<Hotel> = apiClient.getHotels(hotelsSearch)

                Log.d("hotels", "$hotelList")
                Log.d("itinerary", "$flightList")

                val cheapestItinerary : Itinerary? = apiHelper.getCheapestItinerary(flightList)

                val cheapestHotel : Hotel? = apiHelper.getCheapestHotel(hotelList)


                updateCheapestFLightsAndHotel(cheapestItinerary, cheapestHotel)
                updateFlightsRecyclerView(flightList)
                //hotelAdapter.updateHotels(hotelList)
            } catch (e: Exception) {
                Log.e("CityOverviewFragment", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun updateFlightsRecyclerView(flights: MutableList<Itinerary>) {
        flights.map {
            binding.recyclerViewFlights.adapter = FlightAdapter(flights)
        }
    }


    private fun updateCheapestFLightsAndHotel(itinerary: Itinerary?, hotel: Hotel?) {
        Log.d("cheapestHotel", "$hotel")
        Log.d("cheapestItinerary", "$itinerary")

        val depDate = convertDate(itinerary?.outboundDepartureTime)
        val arrDate = convertDate(itinerary?.outboundArrivalTime)
        val hoursMinutes = convertMinutesToHoursAndMinutes(itinerary?.durationOutbound)
        val layover = getLayoverText(itinerary?.stopCountOutbound)
        val totalPrice = itinerary?.rawPrice!! + hotel?.priceRaw!!

        binding.depAirport.text = itinerary?.originId.toString()
        binding.arrAirport.text = itinerary?.destinationId.toString()
        binding.depDate.text = depDate
        binding.arrDate.text = arrDate
        binding.flightLength.text = hoursMinutes
        binding.layovers.text = layover

        binding.hotelName.text = hotel?.name
        binding.address.text = hotel?.relevantPoi
        binding.gradeNumber.text = hotel?.reviewScore.toString()
        binding.gradeWord.text = hotel?.scoreDesc.toString()
        binding.cheapestTotal.text = "€" + totalPrice.toString()

        binding.tempBar.visibility = View.GONE
        binding.cheapestBox.visibility = View.VISIBLE

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDate(date : String?) : String {
        val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("h:mma - MMM dd")
        return dateTime.format(formatter)
    }

    private fun convertMinutesToHoursAndMinutes(totalMinutes: Int?): String {
        val hours = totalMinutes?.div(60)
        val minutes = totalMinutes?.rem(60)
        return "${hours}h ${minutes}m"
    }

    private fun getLayoverText(stops : Int?) : String {
        var string : String = ""
        if (stops!! > 0) {
            string = "$stops layovers"
        } else {
            string = "Direct"
        }
        return string
    }

}
