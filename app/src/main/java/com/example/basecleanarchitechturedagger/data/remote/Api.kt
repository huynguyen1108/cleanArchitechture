package com.example.basecleanarchitechturedagger.data.remote

import com.example.basecleanarchitechturedagger.data.remote.entity.AN0001Request
import com.example.basecleanarchitechturedagger.data.remote.entity.AN0001Response
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET

interface Api {
  @GET("movie/upcoming")
  fun callApiUpComing(@Body body: AN0001Request): Single<AN0001Response>
}
