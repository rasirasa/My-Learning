package com.ssrdi.co.id.myradboox.api.model

data class ResellerResponse(
    val status: String,
    val message: String,
    val data: ResellerOptions
)

data class ResellerOptions(
    val nas: List<ResellerOptionsData>,
    val profile: List<ResellerOptionsData>,
    val server: List<ResellerOptionsData>
)

data class ResellerOptionsData(
    val name: String,
    val value: String
) {
    override fun toString(): String {
        return name
    }
}
