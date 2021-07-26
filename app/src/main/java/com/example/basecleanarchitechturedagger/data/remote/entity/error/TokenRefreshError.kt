package com.example.basecleanarchitechturedagger.data.remote.entity.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenRefreshError(
  override var status: Int,
  val resultCode: String? = null,
  val resultMessage: String? = null,
  val error: String? = null,
  @Json(name = "error_description")
  val errorDescription: String? = null,
  @Json(name = "error_uri")
  val errorUri: String? = null
) : ErrorInterface
