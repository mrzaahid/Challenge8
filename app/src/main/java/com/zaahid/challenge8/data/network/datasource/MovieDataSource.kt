package com.zaahid.challenge8.data.network.datasource


import com.zaahid.challenge8.BuildConfig
import com.zaahid.challenge8.model.MovieRespons
import com.zaahid.challenge8.data.network.service.TMDBApiService

interface MovieDataSource {
    suspend fun searchMovie(query : String,lang: String,page: Int) : MovieRespons
    suspend fun popularMovie( lang: String, page: Int): MovieRespons
}

class MovieDataSourceImpl(private val api : TMDBApiService):
    MovieDataSource {
    //    override suspend fun searchMovie(key: String, query: String, lang: String, page : Int): MovieRespons {
//        return api.searchMovie(key,query,lang,page)
//    }
//
//    override suspend fun popularMovie(
//        key: String,
//        lang: String,
//        page: Int
//    ): MovieRespons {
//        return api.moviePopular(key,lang,page)
//    }
    override suspend fun searchMovie(
        query: String,
        lang: String,
        page: Int
    ): MovieRespons {
        return api.searchMovie(BuildConfig.API_KEY,query,lang,page)
    }

    override suspend fun popularMovie(lang: String, page: Int): MovieRespons {
        return api.moviePopular(BuildConfig.API_KEY,lang,page)
    }


}