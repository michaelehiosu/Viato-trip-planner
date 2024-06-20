package com.michael.viatoapp.model.response.attractions

data class AttractionsResponse(
    var data :  ArrayList<Attractions>
)

data class Attractions (
    var name : String,
    var location_id : String,
    var num_reviews : String,
    var location_string : String,
    var photo : Photo,
    var web_url : String,
    var website : String,
    var address : String,
    var subcategory: ArrayList<SubCategory>,
    var latitude : String,
    var longitude : String
)

data class Photo(
    val images : Image
)

data class Image (
    var large : Large
)

data class Large (
    var url : String
)

data class SubCategory(
    var name : String
)
