package com.jhonatan.appreto.data.network

import com.jhonatan.appreto.data.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiGeo {

    @Headers("Content-Type: application/json")
    @POST("auths/login")
    @JvmSuppressWildcards
    suspend fun login( @Body raw: Map<String, Any>): LoginResponse

}