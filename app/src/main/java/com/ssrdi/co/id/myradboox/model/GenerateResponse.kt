package com.ssrdi.co.id.myradboox.model

data class GenerateResponse (
    val status:String,
    val message : String,
    val data : List<GenerateItemResponse>
)

data class GenerateItemResponse (
    val id : Int,
)