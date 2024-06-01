package com.michael.viatoapp.api

import com.michael.viatoapp.model.data.flights.Country

class ApiHelper {
    public fun filterCountry(countries : MutableList<Country>, budget : String) {
        var flightbudget = budget.toInt() * 60 / 100

//        val filtedCountries = countries.filter { it.cheapestPrice? >= flightbudget  }
    }
}