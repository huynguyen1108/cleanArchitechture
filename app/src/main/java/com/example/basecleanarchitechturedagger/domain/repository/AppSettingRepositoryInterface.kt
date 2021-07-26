package com.example.basecleanarchitechturedagger.domain.repository

interface AppSettingRepositoryInterface {
  fun getAccessToken(): String?
  fun pushAccessToken(token: String?)
  fun clearAccessToken()

}
