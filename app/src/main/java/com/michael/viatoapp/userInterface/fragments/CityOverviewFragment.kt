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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

    }

    override fun onResume() {
        super.onResume()
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
                navigateToMoreInfoFragment()
            }
        }

        binding.buttonGo.setOnClickListener {
            if (onlyHotelSelected || onlyFlightSelected && !cheapestSelected) {
                navigateToMoreInfoFragment()
            }
        }
    }

    private fun fetchFlightsAndHotels() {
        activateProgressBar()
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

                val isbothFlightAndHotel = searchData?.isHotelPressed == searchData?.isFlightPressed
                val onlyFlight = searchData?.isFlightPressed == true && searchData?.isHotelPressed == false
                val onlyHotel = searchData?.isFlightPressed == false && searchData?.isHotelPressed == true

                if(isbothFlightAndHotel) {
                    val flightbudget = searchData?.budget!!.toInt() * 60 /100
                    val hotelBudget = searchData?.budget!!.toInt() * 40 / 100

                    cheapestItinerary  = apiHelper.getCheapestItinerary(flightList, flightbudget)
                    cheapestHotel = apiHelper.getCheapestHotel(hotelList, countrySearch!!, hotelBudget)


                    if (cheapestHotel != null && cheapestItinerary !=null) {
                        setSelectedHotel(cheapestHotel)
                        setSelectedItinerary(cheapestItinerary)

                        updateCheapestFlights(cheapestItinerary)
                        updateCheapestHotel(cheapestItinerary, cheapestHotel)

                        val filteredItinerary = apiHelper.filterItinerary(
                            flightList,
                            cheapestItinerary!!,
                            flightbudget
                        )
                        val filteredHotel =
                            apiHelper.filterHotel(hotelList, countrySearch!!, hotelBudget)

                        updateFlightsRecyclerView(filteredItinerary)
                        updateHotelsRecyclerView(filteredHotel)
                        deactivateProgressBar()
                    }

                    if(cheapestHotel == null && cheapestItinerary == null) {
                        noItinerary()
                    }


                } else if (onlyFlight) {
                    val budget = searchData?.budget!!.toInt()
                    cheapestItinerary  = apiHelper.getCheapestItinerary(flightList,budget)

                    if(cheapestItinerary != null) {
                        setSelectedItinerary(cheapestItinerary)
                        updateCheapestFlights(cheapestItinerary, true)

                        val filteredItinerary = apiHelper.filterItinerary(
                            flightList,
                            cheapestItinerary!!,
                            budget
                        )
                        updateFlightsRecyclerView(filteredItinerary)
                        binding.hotelOnly.visibility = View.GONE
                        binding.recyclerViewHotels.visibility = View.GONE
                        binding.alternativeHotels.visibility = View.GONE
                        deactivateProgressBar()
                    } else {
                        noItinerary()
                    }

                } else if (onlyHotel) {
                    val budget = searchData?.budget!!.toInt()
                    cheapestHotel = apiHelper.getCheapestHotel(hotelList, countrySearch!!, budget)

                    if(cheapestHotel != null) {
                        setSelectedHotel(cheapestHotel)
                        updateCheapestHotel(null, cheapestHotel)

                        val filteredHotel =
                            apiHelper.filterHotel(hotelList, countrySearch!!, budget)
                        updateHotelsRecyclerView(filteredHotel)
                        binding.flightOnly.visibility = View.GONE
                        binding.recyclerViewFlights.visibility = View.GONE
                        binding.alternativeFlights.visibility = View.GONE
                        deactivateProgressBar()
                    } else {
                        noItinerary()
                    }
                }
            } catch (e: Exception) {
                Log.e("CityOverviewFragment", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun noItinerary() {
        binding.cheapestBox.visibility = View.GONE
        binding.noItinerary.visibility = View.VISIBLE
        binding.buttonGo.visibility = View.GONE
        binding.tempBar.visibility = View.GONE
        binding.buttonGoTo.visibility = View.GONE
        binding.alternativeHotels.visibility = View.GONE
        binding.alternativeFlights.visibility = View.GONE
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


    private fun updateCheapestFlights(itinerary: Itinerary?, onlyFlight: Boolean = false) {
        val depDate = convertDate(itinerary?.outboundDepartureTime)
        val arrDate = convertDate(itinerary?.outboundArrivalTime)
        val hoursMinutes = convertMinutesToHoursAndMinutes(itinerary?.durationOutbound)
        val layover = getLayoverText(itinerary?.stopCountOutbound)

        binding.depAirport.text = itinerary?.originId.toString()
        binding.arrAirport.text = itinerary?.destinationId.toString()
        binding.depDate.text = depDate
        binding.arrDate.text = arrDate
        binding.flightLength.text = hoursMinutes
        binding.layovers.text = layover

        if(onlyFlight == true) {
            binding.cheapestTotal.text = "€" + itinerary?.rawPrice.toString()
        }

    }

    private fun updateCheapestHotel(itinerary: Itinerary?, hotel: Hotel?) {
        var duration = getNumberOfDays(countrySearch!!)
        var totalPrice : Double? = hotel?.priceRaw?.toDouble()?.times(duration)

        if(itinerary != null) {
            totalPrice = itinerary?.rawPrice!! + hotel?.priceRaw!! * duration
        }
        binding.hotelName.text = hotel?.name
        binding.address.text = hotel?.relevantPoi
        binding.gradeNumber.text = hotel?.reviewScore.toString()
        binding.gradeWord.text = hotel?.scoreDesc.toString()
        binding.cheapestTotal.text = "€" + totalPrice.toString()
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

    private fun activateProgressBar() {
        binding.cheapestBox.visibility = View.GONE
        binding.tempBar.visibility = View.VISIBLE
    }

    private fun deactivateProgressBar() {
        binding.cheapestBox.visibility = View.VISIBLE
        binding.tempBar.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNumberOfDays(countrySearch: FlightCountriesSearch) : Int {
        val formatter = DateTimeFormatter.ISO_DATE
        val departLocalDate = LocalDate.parse(countrySearch.departDate, formatter)
        val returnLocalDate = LocalDate.parse(countrySearch.returnDate, formatter)

        val daysBetween = ChronoUnit.DAYS.between(departLocalDate, returnLocalDate)
        return daysBetween.toInt()
    }
}
