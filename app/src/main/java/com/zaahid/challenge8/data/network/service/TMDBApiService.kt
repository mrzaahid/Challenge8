package com.zaahid.challenge8.data.network.service

import com.zaahid.challenge8.model.MovieRespons
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {
    @GET("movie/popular")
    suspend fun moviePopular(
        @Query("api_key")api_key : String ,
        @Query("language")language : String ,
        @Query("page")page : Int
    ): MovieRespons

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key")api_key: String ,
        @Query("query",encoded = true)query : String,
        @Query("language") language : String  ,
        @Query("page")page : Int ,
        @Query("include_adult")include_adult :Boolean = false
    ): MovieRespons

}