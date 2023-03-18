package com.example.myapp.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface ProdactAPI {
    @GET ("/{id}")
   suspend fun getInfoById(@Path("id") id: Int) : Info
}