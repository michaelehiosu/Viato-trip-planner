package com.michael.viatoapp.model.response.stays

import com.michael.viatoapp.model.data.stays.City

data class CityResponse (
    var data : ArrayList<City>,
    var status : Boolean,
    var message : String
)