package com.example.basecleanarchitechturedagger.domain.entity

import com.squareup.moshi.Json

data class UpComingRequest(
  @Json(name = "api_key")
  val apiKey: String,
  val language: String
)
