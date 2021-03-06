package com.example.basecleanarchitechturedagger.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenAuthResponse(
  @Json(name = "access_token")
  val accessToken: String,
  @Json(name = "expires_in")
  val expiresIn: String?,
  @Json(name = "token_type")
  val tokenType: String?,
  @Json(name = "refresh_token")
  val refreshToken: String?
)
