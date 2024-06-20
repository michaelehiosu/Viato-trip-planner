package com.michael.viatoapp.api

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ApiHelper {
    private var leastPriceItinerary : Itinerary? = null
    private var leastPriceHotel : Hotel? = null
    public fun filterCountry(countries : MutableList<Country>, budget : String, continent : String) : MutableList<Country> {
        var flightbudget = budget.toInt() * 60 / 100
        var selectedContinent : List<String>

        Log.d("flightbudget", "$flightbudget")

        when (continent) {
            "Africa" -> selectedContinent = africa
            "South America" -> selectedContinent = southAmerica
            "North America" -> selectedContinent = northAmerica
            "Asia" -> selectedContinent = asia
            "Oceania" -> selectedContinent = oceania
            else -> selectedContinent = europe
        }

        val continentFiltered : MutableList<Country> = countries.filter { country ->
            country.name in selectedContinent
        }.toMutableList()

        Log.d("FilteredContinent", "$continentFiltered")

        val budgetFiltered : MutableList<Country> = continentFiltered.filter { country ->
            country.cheapestPrice == null || country.cheapestPrice.toInt() <= flightbudget
        }.toMutableList()

        Log.d("budgetFiltered", "$budgetFiltered")

        return budgetFiltered
    }

    fun getCheapestItinerary(itineraries : MutableList<Itinerary>, budget: Int) : Itinerary? {
        for (itinerary in itineraries) {
            if(leastPriceItinerary == null || itinerary.rawPrice!! < leastPriceItinerary?.rawPrice!!) {
               leastPriceItinerary = itinerary
            }
        }

        if (leastPriceItinerary?.rawPrice!! <= budget) {
            return leastPriceItinerary
        } else {
            return null
        }
    }

    fun filterItinerary(itineraries: MutableList<Itinerary>, cheapestItinerary: Itinerary, flightBudget: Int): MutableList<Itinerary> {
        val newItinerary = ArrayList<Itinerary>()
        var i = 0

        for (itinerary in itineraries) {
            if (i < 10 && itinerary.rawPrice!! <= flightBudget) {
                newItinerary.add(itinerary)
                i++
            }
        }

        return if (newItinerary.size > 0) {
            newItinerary.toMutableList()
        } else {
            emptyList<Itinerary>().toMutableList()
        }
    }


    fun filterHotel(hotels: MutableList<Hotel>, countrySearch: FlightCountriesSearch, hotelBudget: Int) : MutableList<Hotel> {
        var filteredHotel = hotels;
        var newHotel = ArrayList<Hotel>()

        val numberOfDays = getNumberOfDays(countrySearch)
        val dailyBudget = hotelBudget / numberOfDays

        for (hotel in filteredHotel) {
            if(hotel.priceRaw!! <= dailyBudget) {
                hotel.priceRaw = hotel.priceRaw?.toInt()?.times(numberOfDays)
                newHotel.add(hotel)
            }
        }

        if(newHotel.size > 0) {
            return newHotel.toMutableList();
        } else {
            return emptyList<Hotel>().toMutableList()
        }
    }

    fun getCheapestHotel(hotels : MutableList<Hotel>, countrySearch: FlightCountriesSearch, budget: Int) : Hotel? {
        val numberOfDays = getNumberOfDays(countrySearch)
        val dailyBudget = budget / numberOfDays

        for (hotel in hotels) {
            if(leastPriceHotel == null || hotel.priceRaw!! < leastPriceHotel?.priceRaw!!) {
                if(hotel.priceRaw!! <= dailyBudget) {
                    leastPriceHotel = hotel
                }
            }
        }
        return leastPriceHotel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNumberOfDays(countrySearch: FlightCountriesSearch) : Int {
        val formatter = DateTimeFormatter.ISO_DATE
        val departLocalDate = LocalDate.parse(countrySearch.departDate, formatter)
        val returnLocalDate = LocalDate.parse(countrySearch.returnDate, formatter)

        val daysBetween = ChronoUnit.DAYS.between(departLocalDate, returnLocalDate)
        return daysBetween.toInt()
    }



    val africa = arrayListOf("Algeria",
        "Angola",
        "Benin",
        "Botswana",
        "Burkina Faso",
        "Burundi",
        "Cabo Verde",
        "Cameroon",
        "Central African Republic",
        "Chad",
        "Comoros",
        "Democratic Republic of the Congo",
        "Republic of the Congo",
        "Djibouti",
        "Egypt",
        "Equatorial Guinea",
        "Eritrea",
        "Eswatini",
        "Ethiopia",
        "Gabon",
        "Gambia",
        "Ghana",
        "Guinea",
        "Guinea-Bissau",
        "Ivory Coast",
        "Kenya",
        "Lesotho",
        "Liberia",
        "Libya",
        "Madagascar",
        "Malawi",
        "Mali",
        "Mauritania",
        "Mauritius",
        "Morocco",
        "Mozambique",
        "Namibia",
        "Niger",
        "Nigeria",
        "Rwanda",
        "Sao Tome and Principe",
        "Senegal",
        "Seychelles",
        "Sierra Leone",
        "Somalia",
        "South Africa",
        "South Sudan",
        "Sudan",
        "Tanzania",
        "Togo",
        "Tunisia",
        "Uganda",
        "Zambia",
        "Zimbabwe")

    val europe =  arrayListOf(
        "Albania",
        "Andorra",
        "Armenia",
        "Austria",
        "Azerbaijan",
        "Belarus",
        "Belgium",
        "Bosnia and Herzegovina",
        "Bulgaria",
        "Croatia",
        "Cyprus",
        "Czech Republic",
        "Denmark",
        "Estonia",
        "Finland",
        "France",
        "Georgia",
        "Germany",
        "Greece",
        "Hungary",
        "Iceland",
        "Ireland",
        "Italy",
        "Kazakhstan",
        "Kosovo",
        "Latvia",
        "Liechtenstein",
        "Lithuania",
        "Luxembourg",
        "Malta",
        "Moldova",
        "Monaco",
        "Montenegro",
        "Netherlands",
        "North Macedonia",
        "Norway",
        "Poland",
        "Portugal",
        "Romania",
        "Russia",
        "San Marino",
        "Serbia",
        "Slovakia",
        "Slovenia",
        "Spain",
        "Sweden",
        "Switzerland",
        "Turkey",
        "Ukraine",
        "United Kingdom",
        "Vatican City"
    )

    val oceania = arrayListOf(
        "Australia",
        "Fiji",
        "Kiribati",
        "Marshall Islands",
        "Micronesia",
        "Nauru",
        "New Zealand",
        "Palau",
        "Papua New Guinea",
        "Samoa",
        "Solomon Islands",
        "Tonga",
        "Tuvalu",
        "Vanuatu"
    )

    val southAmerica = arrayListOf(
        "Argentina",
        "Bolivia",
        "Brazil",
        "Chile",
        "Colombia",
        "Ecuador",
        "Guyana",
        "Paraguay",
        "Peru",
        "Suriname",
        "Uruguay",
        "Venezuela"
    )

    val northAmerica = arrayListOf(
    "Antigua and Barbuda",
    "Bahamas",
    "Barbados",
    "Belize",
    "Canada",
    "Costa Rica",
    "Cuba",
    "Dominica",
    "Dominican Republic",
    "El Salvador",
    "Grenada",
    "Guatemala",
    "Haiti",
    "Honduras",
    "Jamaica",
    "Mexico",
    "Nicaragua",
    "Panama",
    "Saint Kitts and Nevis",
    "Saint Lucia",
    "Saint Vincent and the Grenadines",
    "Trinidad and Tobago",
    "United States"
    )

    val asia = arrayListOf(
        "Afghanistan",
        "Armenia",
        "Azerbaijan",
        "Bahrain",
        "Bangladesh",
        "Bhutan",
        "Brunei",
        "Cambodia",
        "China",
        "Cyprus",
        "Georgia",
        "India",
        "Indonesia",
        "Iran",
        "Iraq",
        "Israel",
        "Japan",
        "Jordan",
        "Kazakhstan",
        "Kuwait",
        "Kyrgyzstan",
        "Laos",
        "Lebanon",
        "Malaysia",
        "Maldives",
        "Mongolia",
        "Myanmar",
        "Nepal",
        "North Korea",
        "Oman",
        "Pakistan",
        "Palestine",
        "Philippines",
        "Qatar",
        "Saudi Arabia",
        "Singapore",
        "South Korea",
        "Sri Lanka",
        "Syria",
        "Taiwan",
        "Tajikistan",
        "Thailand",
        "Timor-Leste",
        "Turkey",
        "Turkmenistan",
        "United Arab Emirates",
        "Uzbekistan",
        "Vietnam",
        "Yemen"
    )

}