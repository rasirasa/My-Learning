package com.ssrdi.co.id.myradboox.model

data class VoucherResponseDetail(
    val status:String,
    val message : String,
    val data : List<VoucherItemDetailResponse>
)

data class VoucherItemDetailResponse(
    val id : Int,
    val username : String,
    val password : String,
    val profile : String,
    val nas : String,
    val server : String,
    val status : String,
    val create_time : String,
    val start_time : String,
    val end_time : String
)


