package com.michael.viatoapp.model.response.flights

data class CitiesResponse(
    var data : CityData,
    var status : Boolean,
    var message : String
)
data class CityData (
    var countryDestination : CountryDestination
)

data class CountryDestination (
    var results : ArrayList<CityResult>
)

data class CityResult (
    var entityId : String,
    var skyId : String,
    var content : CityContent
)

data class CityContent (
    var location : CityLocation,
    var image : cityImage
)

data class CityLocation (
    var name : String,
)

data class cityImage (
    var url : String
)



