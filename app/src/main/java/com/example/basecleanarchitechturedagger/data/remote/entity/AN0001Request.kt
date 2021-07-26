package com.example.basecleanarchitechturedagger.data.remote.entity

import com.example.basecleanarchitechturedagger.domain.entity.UpComingRequest
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AN0001Request(
  val apiKey: String,
  val language: String
) {
  companion object {
    fun fromEntity(request: UpComingRequest): AN0001Request {
      return AN0001Request(
        apiKey = request.apiKey,
        language = request.language
      )
    }
  }
}
