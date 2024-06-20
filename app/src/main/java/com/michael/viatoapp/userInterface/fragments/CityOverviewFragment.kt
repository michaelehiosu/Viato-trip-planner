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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.api.ApiHelper
import com.michael.viatoapp.databinding.ActivityCityOverviewBinding
import com.michael.viatoapp.model.data.SearchData
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
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
    private var countrySearch : FlightCountriesSearch? = null
    private val apiClient = ApiClient()
    private val apiHelper = ApiHelper()
    private var cheapestItinerary : Itinerary? = null
    private var cheapestHotel : Hotel? = null
    private var selectedItinerary : Itinerary? = null
    private var selectedHotel : Hotel? = null
    private var cheapestSelected : Boolean = true
    private var onlyFlightSelected : Boolean = false
    private var onlyHotelSelected : Boolean = false
    private var searchData: SearchData? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            city = it.getSerializable("city") as City?
            countrySearch = it.getSerializable("countrySearch") as FlightCountriesSearch
            searchData = it.getSerializable("searchData") as SearchData
        }
        binding = ActivityCityOverviewBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flightAdapter = FlightAdapter(mutableListOf()) { itinerary ->
            handleFlightSelection(itinerary)
        }
        hotelAdapter = HotelAdapter(mutableListOf()){ hotel ->
            handleHotelSelection(hotel)
        }

        Log.d("Search Data:", "Data: $searchData")

        binding.recyclerViewFlights.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFlights.adapter = flightAdapter

        binding.recyclerViewHotels.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHotels.adapter = hotelAdapter

        binding.llCheapest.setOnClickListener {
            toggleCheapestSelected()
        }

        if(city != null && countrySearch != null) {
            fetchFlightsAndHotels()
        }

        Glide.with(this)
            .load(city?.imageUrl)
            .placeholder(R.drawable.rio_pic)
            .into(binding.cityImage)

        binding.cityTextview.text = city?.name

        binding.buttonGoTo.setOnClickListener {
            if (cheapestSelected && !onlyFlightSelected && !onlyHotelSelected) {
                // TODO: Send selected hotel and selected itinerary to the more info page
                navigateToMoreInfoFragment()
            }
        }

        binding.buttonGo.setOnClickListener {
            if (onlyHotelSelected || onlyFlightSelected && !cheapestSelected) {
                // TODO: Send selected hotel and selected itinerary to the more info page
                navigateToMoreInfoFragment()
            }
        }
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

                cheapestItinerary  = apiHelper.getCheapestItinerary(flightList)
                cheapestHotel = apiHelper.getCheapestHotel(hotelList)
                val filteredItinerary = apiHelper.filterItinerary(flightList, cheapestItinerary!!)
                val filteredHotel = apiHelper.filterHotel(hotelList, countrySearch!!)

                setSelectedHotel(cheapestHotel)
                setSelectedItinerary(cheapestItinerary)

                updateCheapestFLightsAndHotel(cheapestItinerary, cheapestHotel)
                updateFlightsRecyclerView(filteredItinerary)
                updateHotelsRecyclerView(filteredHotel)
            } catch (e: Exception) {
                Log.e("CityOverviewFragment", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun updateFlightsRecyclerView(flights: MutableList<Itinerary>) {
        flights.map {
            binding.recyclerViewFlights.adapter = FlightAdapter(flights){ itinerary ->
                handleFlightSelection(itinerary)
            }
        }
    }

    private fun updateHotelsRecyclerView(hotels: MutableList<Hotel>) {
        hotels.map {
            binding.recyclerViewHotels.adapter = HotelAdapter(hotels) { hotel ->
                handleHotelSelection(hotel)
            }
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

    private fun setSelectedItinerary(itinerary: Itinerary?) {
        selectedItinerary = itinerary
    }

    private fun setSelectedHotel(hotel: Hotel?) {
        selectedHotel = hotel
    }

    private fun toggleCheapestSelected() {
        val cheap = !cheapestSelected
        if (cheap) {
            binding.llCheapest.setBackgroundResource(R.drawable.cheapest_bg)
            cheapestSelected = !cheapestSelected
            selectedHotel = cheapestHotel
            selectedItinerary = cheapestItinerary
            flightAdapter.clearSelection()
            hotelAdapter.clearSelection()
            resetBackgroundsInRecyclerView(binding.recyclerViewFlights, binding.recyclerViewHotels)
            onlyHotelSelected = false
            onlyFlightSelected = false
        } else {
            binding.llCheapest.background = null
            cheapestSelected = !cheapestSelected
            selectedHotel = null
            selectedItinerary = null
        }
    }

    private fun handleFlightSelection(itinerary: Itinerary?) {
        if (itinerary == null) {
            // Deselecting the item
            selectedItinerary = null
            onlyFlightSelected = false
        } else {
            // Selecting the item
            selectedItinerary = itinerary
            onlyFlightSelected = true
            binding.llCheapest.background = null
            cheapestSelected = false
        }
    }

    private fun handleHotelSelection(hotel: Hotel?) {
        if (hotel == null) {
            // Deselecting the item
            selectedHotel = null
            onlyHotelSelected = false
        } else {
            // Selecting the item
            selectedHotel = hotel
            onlyHotelSelected = true
            binding.llCheapest.background = null
            cheapestSelected = false
        }
    }

    private fun resetBackgroundsInRecyclerView(recyclerView: RecyclerView, recyclerViewHotel: RecyclerView) {
        for (i in 0 until recyclerView.childCount) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? FlightAdapter.ViewHolder
            viewHolder?.linearView?.background = null
        }

        for (i in 0 until recyclerViewHotel.childCount) {
            val viewHolder = recyclerViewHotel.findViewHolderForAdapterPosition(i) as? HotelAdapter.HotelViewHolder
            viewHolder?.linearView?.background = null
        }

    }

    private fun navigateToMoreInfoFragment() {
        val context = activity
        if (context is MainNavigationActivity) {
            // Create a bundle to hold the selected data
            val bundle = Bundle().apply {
                putSerializable("selectedItinerary", selectedItinerary)
                putSerializable("selectedHotel", selectedHotel)
            }
            context.navigateToMoreInfoFragment(bundle)
        }
    }
}
