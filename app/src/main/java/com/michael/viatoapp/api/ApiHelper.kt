package com.michael.viatoapp.api

import android.util.Log
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.response.Flight

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

    fun getCheapestItinerary(itineraries : MutableList<Itinerary>) : Itinerary? {
        for (itinerary in itineraries) {
            if(leastPriceItinerary == null || itinerary.rawPrice!! < leastPriceItinerary?.rawPrice!!) {
               leastPriceItinerary = itinerary
            }
        }
        return leastPriceItinerary
    }

    fun getCheapestHotel(hotels : MutableList<Hotel>) : Hotel? {
        for (hotel in hotels) {
            if(leastPriceHotel == null || hotel.priceRaw!! < leastPriceHotel?.priceRaw!!) {
                leastPriceHotel = hotel
            }
        }
        return leastPriceHotel
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