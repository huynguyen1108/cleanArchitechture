package com.example.basecleanarchitechturedagger.data.repository

import com.example.basecleanarchitechturedagger.data.remote.RefreshTokenApi
import com.example.basecleanarchitechturedagger.data.remote.entity.TokenAuthResponse
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call

class TokenRepository(private val api: RefreshTokenApi) : TokenRepositoryInterface {
  override fun refresh(currentRefreshToken: String?): Call<TokenAuthResponse> {
    val requestBody = createBody(currentRefreshToken)
    return api.refreshToken(requestBody)
  }

  private fun createBody(currentRefreshToken: String?): RequestBody {
    return RequestBody.create(
      MediaType.parse("application/x-www-form-urlencoded"),
      StringBuilder()
        .append("grant_type=")
        .append("refresh_token")
        .append("&")
        .append("refresh_token")
        .append(currentRefreshToken ?: "")
        .toString()
    )
  }
}
