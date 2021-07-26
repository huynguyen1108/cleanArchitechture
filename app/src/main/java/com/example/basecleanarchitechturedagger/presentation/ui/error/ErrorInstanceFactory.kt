package com.example.basecleanarchitechturedagger.presentation.ui.error

import android.content.Context
import com.example.basecleanarchitechturedagger.data.remote.entity.error.*
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import com.example.basecleanarchitechturedagger.utility.Util
import com.squareup.moshi.Moshi
import io.reactivex.exceptions.CompositeException
import retrofit2.HttpException
import java.io.IOException

class ErrorInstanceFactory {
  companion object {
    fun getErrorInstance(error: Error, context: Context): ErrorInterface {
      return when (val throwable = getSimpleThrowable(error.throwable)) {
        is IOException -> {
          if (Util.isNetworkConnected(context)) {
            TimeoutError()
          } else {
            NetworkError()
          }
        }
        is TokenRefreshException -> {
          createTokenRefreshError(error.throwable) ?: UnknownError()
        }
        is HttpException -> {
          createError(error.throwable) ?: UnknownError()
        }
        else -> {
          UnknownError()
        }
      }
    }

    private fun getSimpleThrowable(throwable: Throwable): Throwable {
      return when (throwable) {
        is CompositeException -> {
          throwable.exceptions.firstOrNull()?.let {
            getSimpleThrowable(it)
          } ?: throwable
        }
        else -> throwable
      }
    }

    private fun createTokenRefreshError(throwable: Throwable): ErrorInterface? {
      if (throwable !is TokenRefreshException) return null
      return try {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(TokenRefreshError::class.java)
        jsonAdapter.fromJson(throwable.response()?.errorBody()?.charStream().toString())?.apply {
          this.status = throwable.code()
        }
      } catch (e: Exception) {
        TokenRefreshError(throwable.code())
      }
    }

    private fun createError(throwable: Throwable): ErrorInterface? {
      if (throwable !is HttpException) return null
      return try {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(AppServerError::class.java)
        jsonAdapter.fromJson(throwable.response()?.errorBody()?.charStream().toString())?.apply {
          this.status = throwable.code()
        }
      } catch (e: Exception) {
        AppServerError(throwable.code())
      }
    }
  }
}
