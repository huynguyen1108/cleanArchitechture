package com.example.basecleanarchitechturedagger.presentation.ui.error

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.basecleanarchitechturedagger.data.remote.ApiEnum
import com.example.basecleanarchitechturedagger.data.remote.entity.error.ErrorInterface

abstract class ApiError(val api: ApiEnum, val targetClassName: String? = null) {

  data class Result(val api: ApiEnum, val errorType: ErrorType)

  sealed class ErrorType {
    object Unknown : ErrorType()

    object NotFound : ErrorType()

    data class TokenRefresh(val statusCode: Int?) : ErrorType()

    data class Local(val status: Int?, val title: String? = null, val message: String? = null) : ErrorType()

    data class JustClose(val title: String? = null, val message: String? = null) : ErrorType()

    data class BackToLogin(val title: String? = null, val message: String? = null) : ErrorType()

    data class Retry(val title: String? = null, val message: String? = null) : ErrorType()

    data class RetryBackToLogin(val title: String? = null, val message: String? = null) : ErrorType()
  }

  abstract fun getErrorType(context: Context, error: ErrorInterface): ErrorType

  companion object {
    inline fun <reified T : Fragment> getTargetClassName(): String = T::class.java.name
    fun getTargetClassName(fragment: Fragment): String = fragment::class.java.name
    fun getTargetClassName(activity: Activity): String = activity::class.java.name
  }
}
