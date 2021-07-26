package com.example.basecleanarchitechturedagger.data.remote

import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import okhttp3.Interceptor
import okhttp3.Response

class RefreshTokenAuthInterceptor constructor(
  private val appSettingRepository: AppSettingRepositoryInterface
) : Interceptor{
  override fun intercept(chain: Interceptor.Chain): Response {
    return chain.proceed(
      chain.request().run {
        newBuilder()
          .addHeader("Content-Type", "application/json")
          .addHeader("Accept", "application/json")
          .apply {
            appSettingRepository.getAccessToken()?.also {
              if(it.isNotEmpty()) {
                addHeader("authorization", "Bearer $it")
              }
            }
          }
          .method(method(), body())
          .build()
      }
    )
  }

}
