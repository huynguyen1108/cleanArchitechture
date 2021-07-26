package com.example.basecleanarchitechturedagger.presentation.ui.error

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.basecleanarchitechturedagger.data.remote.ApiEnum
import com.example.basecleanarchitechturedagger.data.remote.entity.error.AppServerError
import com.example.basecleanarchitechturedagger.data.remote.entity.error.NetworkError
import com.example.basecleanarchitechturedagger.data.remote.entity.error.TimeoutError
import com.example.basecleanarchitechturedagger.data.remote.entity.error.TokenRefreshError
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface
import timber.log.Timber
import javax.inject.Inject

class ApiErrorHandler @Inject constructor(
  private val appSettingRepository: AppSettingRepositoryInterface,
  private val tokenRepository: TokenRepositoryInterface
) {

//  private val errorResultListener

  fun show(activity: AppCompatActivity?, error: Error) {
    activity?.also {
      showInner(it.applicationContext, ApiError.getTargetClassName(it), it.supportFragmentManager, error)
    }
  }

  fun show(fragment: Fragment?, error: Error) {
    fragment?.let {
      showInner(it.context, ApiError.getTargetClassName(it), it.childFragmentManager, error)
    }
  }

  private fun showInner(
    context: Context?,
    targetClassName: String,
    fragmentManager: FragmentManager,
    error: Error,
    errorType: ApiError.ErrorType? = null
  ) {
    if (context == null) {
      Timber.e("context null")
      return
    }
  }

  private fun getErrorType(
    error: Error,
    context: Context,
    targetClassName: String? = null
  ): ApiError.ErrorType {
    return when (val errorInstance = ErrorInstanceFactory.getErrorInstance(error, context)) {
      is NetworkError -> {
        ApiError.ErrorType.Retry(
          title = "Không thể kết nối mạng",
          message = "Vui lòng kiểm tra lại internet"
        )
      }
      is TimeoutError -> {
        ApiError.ErrorType.Retry(
          title = "Lỗi",
          message = "Xin lỗi vì đã xảy ra lỗi nhưng bạn vui lòng thử lại"
        )
      }
      is TokenRefreshError -> {
        ApiError.ErrorType.TokenRefresh(errorInstance.status)
      }
      is AppServerError -> {
        targetClassName ?: return ApiError.ErrorType.Unknown
        (errorInstance as? AppServerError).let { appServerError ->
          appServerError?.errors?.sortWith(compareBy({it.code}, {it.field}))
          Timber.e("${appServerError?.errors}")
        }
        when(error.api) {
          ApiEnum.AN0001 -> AN0001Error(targetClassName).getErrorType(context, errorInstance)
          else -> ApiError.ErrorType.Unknown
        }
      }
      else -> {
        ApiError.ErrorType.Unknown
      }
    }
  }
}
