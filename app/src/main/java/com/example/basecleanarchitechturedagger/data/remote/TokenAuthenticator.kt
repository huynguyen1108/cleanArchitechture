package com.example.basecleanarchitechturedagger.data.remote

import com.example.basecleanarchitechturedagger.data.remote.entity.error.TokenRefreshException
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import kotlin.concurrent.withLock

private const val AUTHORIZATION_HEADER_FORMAT = "Bearer %s"

class TokenAuthenticator @Inject constructor(
  private val tokenRepository: TokenRepositoryInterface,
  private val appSettingRepository: AppSettingRepositoryInterface
) : Authenticator {

  private val lock: ReentrantLock = ReentrantLock(true)

  override fun authenticate(route: Route?, response: Response): Request? {
    Timber.e("$response")

    if (response.code() == 401) {
      return lock.withLock {
        val authorizationHeader = response.request().header("authorization")
        val currentAccessToken = appSettingRepository.getAccessToken()

        if (currentAccessToken.isNullOrEmpty() ||
          authorizationHeader == AUTHORIZATION_HEADER_FORMAT.format(currentAccessToken)
        ) {
          refreshAccessToken()
        }

        val accessToken = appSettingRepository.getAccessToken()

        response.request().newBuilder()
          .apply {
            if (!accessToken.isNullOrEmpty()) {
              removeHeader("authorization")
              addHeader("authorization", AUTHORIZATION_HEADER_FORMAT.format(accessToken))
            }
          }.build()
      }
    }
    return null
  }

  private fun refreshAccessToken() {
    tokenRepository.refresh(appSettingRepository.getAccessToken()).execute().let {
      if (it.isSuccessful && it.code() == 200) {
        appSettingRepository.apply {
          pushAccessToken(it.body()!!.accessToken)
        }
      } else {
        throw TokenRefreshException(it)
      }
    }
  }
}
