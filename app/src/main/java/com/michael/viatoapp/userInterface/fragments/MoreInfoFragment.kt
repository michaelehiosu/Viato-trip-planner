package com.michael.viatoapp.userInterface.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.databinding.ActivityMoreInfoBinding
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.flights.ItineraryDetails
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.data.stays.HotelPrice
import com.michael.viatoapp.model.request.flights.FlightDetailsSearch
import com.michael.viatoapp.model.request.stays.HotelPricesSearch
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.text.util.Linkify
import com.bumptech.glide.Glide

class MoreInfoFragment : Fragment() {
    private lateinit var binding: ActivityMoreInfoBinding
    private var selectedHotel: Hotel? = null
    private var selectedItinerary: Itinerary? = null
    private var cityInfo: City? = null
    private val apiClient = ApiClient()
    private var departure : Map<String, String>? = null
    private var arrival : Map<String, String>? = null
    private var inboundDeparture : Map<String, String>? = null
    private var inboundArrival : Map<String, String>? = null
    private var flightPrice : String? = null
    private var totalPrice : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            selectedItinerary = it.getSerializable("selectedItinerary") as? Itinerary
            selectedHotel = it.getSerializable("selectedHotel") as? Hotel
            cityInfo = it.getSerializable("cityInfo") as? City
        }
        Log.d("hotel info:", "$selectedHotel")
        Log.d("flight info:", "$selectedItinerary")
        binding = ActivityMoreInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        if(selectedItinerary != null) {
            fetchFlightDetails()
        }

        if(selectedHotel != null) {
            fetchHotelPrices()
        }

        manageHotelAndFlightView()

        totalPrice = getTotalPrice()
        binding.tvCityName.setText(cityInfo?.name)
        binding.totalPrice.setText(totalPrice.toString())

        Glide.with(this)
            .load(cityInfo?.imageUrl)
            .placeholder(R.drawable.rio_pic)
            .into(binding.cityPic)
    }

    private fun getTotalPrice() : String {
        if(selectedItinerary != null && selectedHotel != null) {
            return  "€" + ((selectedItinerary?.rawPrice?.toInt() ?: 0) + selectedHotel?.priceRaw!!).toString();
        } else if (selectedItinerary == null && selectedHotel != null) {
            return "€" + (selectedHotel?.priceRaw!!).toString()
        } else if (selectedItinerary != null && selectedHotel == null) {
           return "€" + (selectedItinerary?.rawPrice?.toInt() ?: 0)
        } else {
            return ""
        }
    }

    private fun manageHotelAndFlightView() {
        if(selectedItinerary == null && selectedHotel != null) {
            binding.llFlightInfo.visibility = View.GONE
            binding.tvFlightInfo.visibility = View.GONE
            binding.tvTicketLink.visibility = View.GONE
        } else if (selectedHotel == null && selectedItinerary != null) {
            binding.llHotelInfo.visibility = View.GONE
            binding.tvHotelInfo.visibility= View.GONE
            binding.tvStayLink.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setFlightInformation(flightDetails: ItineraryDetails?) {
        Log.d("d", "$flightDetails")
        departure = selectedItinerary?.outboundDepartureTime?.let { separateDateTime(it) }
        arrival = selectedItinerary?.outboundArrivalTime?.let { separateDateTime(it) }
        inboundDeparture = selectedItinerary?.inboundDepartureTime?.let { separateDateTime(it) }
        inboundArrival = selectedItinerary?.inboundArrivalTime?.let { separateDateTime(it) }
        flightPrice = "€" + selectedItinerary?.rawPrice.toString()

        binding.flightPrice.setText(flightPrice)

        binding.depTime.setText(departure?.get("time"))
        binding.depDate.setText(selectedItinerary?.originId + " - " + departure?.get("date"))

        binding.arrTime.setText(arrival?.get("time"))
        binding.arrDate.setText(selectedItinerary?.destinationId + " - " + arrival?.get("date"))

        binding.depReturnTime.setText(inboundDeparture?.get("time"))
        binding.depReturnDate.setText(selectedItinerary?.destinationId + " - " + inboundDeparture?.get("date"))

        binding.arrReturnTime.setText(inboundArrival?.get("time"))
        binding.arrReturnDate.setText(selectedItinerary?.originId + " - " + inboundArrival?.get("date"))

        binding.flightLenght.setText(selectedItinerary?.durationOutbound?.let { formatDuration(it) })
        binding.returnFlightLenght.setText(selectedItinerary?.durationInbound?.let { formatDuration(it) })

        val bookingUrl = flightDetails?.bookingUrl
        binding.tvTicketLink.text = "Buy your tickets here"

        Linkify.addLinks(binding.tvTicketLink, Linkify.WEB_URLS)

        val textView = binding.tvTicketLink
        textView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingUrl))
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setHotelInformation(prices: MutableList<HotelPrice>) {
        Log.d("d", "$prices")

        binding.hotelName.setText(selectedHotel?.name)
        binding.address.setText(selectedHotel?.relevantPoi)

        binding.gradeWord.setText(selectedHotel?.scoreDesc + ": ")
        binding.gradeNumber.setText(selectedHotel?.reviewScore.toString() + "/5")

        binding.roomPolicies.setText(prices[0].roomPolicies)
        binding.roomType.setText(prices[0].roomType)

        binding.hotelPrice.setText("€" + selectedHotel?.priceRaw.toString())
        binding.pricePerNight.setText("€" + prices[0].rawPrice.toString())

        val stayUrl = "https://" + prices[0].deeplink
        binding.tvStayLink.text = "Book your stay here"

        Linkify.addLinks(binding.tvStayLink, Linkify.WEB_URLS)

        val textView = binding.tvStayLink
        textView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stayUrl))
            startActivity(intent)
        }
    }

    private fun fetchFlightDetails() {
        lifecycleScope.launch {
            val flightDetailsSearch = FlightDetailsSearch(
                token = selectedItinerary?.token,
                itineraryId = selectedItinerary?.id,
                currency = "EUR", // replace when user data is available everywhere
                dummy = true,
            )

            try {
                val flightDetails: ItineraryDetails? = apiClient.getFlightDetails(flightDetailsSearch)

                setFlightInformation(flightDetails)
            } catch (e: Exception) {
                Log.e("MoreInfoFragment", "Error fetching data :${e.message}")
            }
        }
    }

    private fun fetchHotelPrices() {
        lifecycleScope.launch {
            val hotelPrices = HotelPricesSearch(
                hotelId = selectedHotel?.hotelId,
                checkIn = selectedItinerary?.outboundDepartureTime,
                checkOut = selectedItinerary?.inboundDepartureTime,
                dummy = true,
            )

            try {
                val hotelPrices: MutableList<HotelPrice> = apiClient.getHotelPrices(hotelPrices)

                setHotelInformation(hotelPrices)
            } catch (e: Exception) {
                Log.e("MoreInfoFragment", "Error fetching data :${e.message}")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun separateDateTime(dateTime: String): Map<String, String> {
        // Define the expected input format
        val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        // Parse the datetime string
        val parsedDateTime = LocalDateTime.parse(dateTime, inputFormatter)

        // Define the output formats
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Format the date and time separately
        val date = parsedDateTime.format(dateFormatter)
        val time = parsedDateTime.format(timeFormatter)

        // Return the separated date and time in a map
        return mapOf("date" to date, "time" to time)
    }

    private fun formatDuration(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return "${hours}h ${remainingMinutes}m"
    }

}