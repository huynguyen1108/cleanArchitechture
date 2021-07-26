package com.example.basecleanarchitechturedagger.data.remote.entity

import com.example.basecleanarchitechturedagger.domain.entity.RegisterRequest
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountsRequest(
  val mail: String,
  val password: String
) {

  companion object {
    fun fromEntity(request: RegisterRequest): AccountsRequest {
      return AccountsRequest(
        mail = request.mail,
        password = request.password
      )
    }
  }
}
