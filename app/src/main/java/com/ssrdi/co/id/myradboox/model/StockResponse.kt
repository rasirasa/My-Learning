package com.ssrdi.co.id.myradboox.model

data class StockResponse(
    val status : String,
    val message : String,
    val data : List<StockItemResponse>
)
data class StockItemResponse(
    val new : Int,
    val active : Int,
    val expire : Int,
    val total : Int,
)
