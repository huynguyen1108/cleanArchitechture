package com.example.basecleanarchitechturedagger.data.remote

import com.example.basecleanarchitechturedagger.data.remote.entity.TokenAuthResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RefreshTokenApi {
  @Headers("Content-Type: application/x-www-form-urlencoded")
  @POST("auth/token")
  fun refreshToken(@Body body: RequestBody): Call<TokenAuthResponse>
}
