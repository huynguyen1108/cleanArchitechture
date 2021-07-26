package com.example.basecleanarchitechturedagger.domain.repository

import com.example.basecleanarchitechturedagger.data.remote.entity.TokenAuthResponse
import retrofit2.Call

interface TokenRepositoryInterface {
  fun refresh(currentRefreshToken: String?): Call<TokenAuthResponse>
}
