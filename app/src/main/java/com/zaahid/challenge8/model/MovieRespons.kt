package com.zaahid.challenge8.model

import com.google.gson.annotations.SerializedName

data class MovieRespons (
    @SerializedName("page")
    val page : Int?,
    @SerializedName("results")
    val results : List<Hasil>,
    @SerializedName("totalPages")
    val totalPages : Int?,
    @SerializedName("totalResult")
    val totalResult : Int?
)