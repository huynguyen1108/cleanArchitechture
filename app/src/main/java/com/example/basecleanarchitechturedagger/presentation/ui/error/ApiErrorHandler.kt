package com.example.basecleanarchitechturedagger.presentation.ui.error

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.basecleanarchitechturedagger.Application
import com.example.basecleanarchitechturedagger.BuildConfig
import com.example.basecleanarchitechturedagger.data.remote.ApiEnum
import com.example.basecleanarchitechturedagger.data.remote.entity.error.*
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface
import com.example.basecleanarchitechturedagger.presentation.ui.dialog.Dialog
import com.example.basecleanarchitechturedagger.presentation.ui.dialog.TokenRefreshErrorDialog
import com.example.basecleanarchitechturedagger.presentation.ui.main.fragment.MainFragment
import timber.log.Timber
import javax.inject.Inject

class ApiErrorHandler @Inject constructor(
  private val appSettingRepository: AppSettingRepositoryInterface,
  private val tokenRepository: TokenRepositoryInterface
) {

  private var errorResultListener: ((ApiError.Result) -> Unit)? = null
  private var closeClickListener: ((ApiEnum, ApiError.ErrorType) -> Unit)? = null
  private var showListener: DialogInterface.OnShowListener? = null
  private var dismissListener: DialogInterface.OnDismissListener? = null
  private var retryListener: ((Error, ApiError.ErrorType) -> Unit)? = null

  fun setOnErrorResultListener(listener: (result: ApiError.Result) -> Unit) {
    errorResultListener = listener
  }

  fun setOnCloseClickListener(listener: (api: ApiEnum, errorType: ApiError.ErrorType) -> Unit) {
    closeClickListener = listener
  }

  fun setOnShowListener(listener: DialogInterface.OnShowListener) {
    showListener = listener
  }

  fun setOnDismissListener(listener: DialogInterface.OnDismissListener) {
    dismissListener = listener
  }

  fun setOnRetryListener(listener: ((error: Error, errorType: ApiError.ErrorType) -> Unit)?) {
    retryListener = listener
  }

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

  fun show(fragment: Fragment?, error: Error, errorType: ApiError.ErrorType) {
    fragment?.also {
      showInner(it.context, ApiError.getTargetClassName(it), it.childFragmentManager, error, errorType)
    }
  }

  fun showLocalError(fragment: Fragment?, error: Error) {
    val nonNullFragment = fragment ?: return
    val throwable = error.throwable as LocalException
    val localErrorType = ApiError.ErrorType.Local(
      status = throwable.status.value,
      title = throwable.title,
      message = throwable.message
    )
    Dialog.createCloseDialog(
      title = throwable.title,
      message = throwable.message,
      positiveClickListener = closeClickListener(error.api, localErrorType)
    ).run {
      setOnShowListener(showListener)
      setOnDismissListener(dismissListener)
      show(nonNullFragment.childFragmentManager)
    }
  }

  private fun showInner(
    context: Context?,
    targetClassName: String,
    fragmentManager: FragmentManager,
    error: Error,
    apiErrorType: ApiError.ErrorType? = null
  ) {
    if (context == null) {
      Timber.e("context null")
      return
    }

    when (val errorType = apiErrorType ?: getErrorType(error, context,targetClassName)) {
      is ApiError.ErrorType.TokenRefresh -> {
        Dialog.createTokenRefreshError(
          context,
          fragmentManager,
          errorType.statusCode,
          object : TokenRefreshErrorDialog.RetryListener {
            override fun onSuccess() {
              Timber.d("retry success")
              errorResultListener?.invoke(ApiError.Result(error.api, errorType))
            }
          },
          tokenRepository,
          appSettingRepository
        )?.run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
      is ApiError.ErrorType.JustClose -> {
        Dialog.createCloseDialog(
          title = errorType.title,
          message = errorType.message,
          positiveClickListener = closeClickListener(error.api, errorType)
        ).run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
      is ApiError.ErrorType.Back -> {
        Dialog.createCloseDialog(
          title = errorType.title,
          message = errorType.message,
          positiveClickListener = closeClickListener(error.api, errorType)
        ).run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
      is ApiError.ErrorType.Retry -> {
        Dialog.createRetryDialog(
          title = errorType.title,
          message = errorType.message,
          positiveClickListener = retryClickListener(error, errorType),
          negativeClickListener = closeClickListener(error.api, errorType)
        ).run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
      is ApiError.ErrorType.RetryToContinue -> {
        Dialog.createRetryDialogSingleButton(
          title = errorType.title,
          message = errorType.message,
          positiveClickListener = retryClickListener(error, errorType)
        ).run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
      is ApiError.ErrorType.RetryBackToLogin -> {
        Dialog.createRetryDialog(
          title = errorType.title,
          message = errorType.message,
          positiveClickListener = DialogInterface.OnClickListener { _, _ -> forceClose(context) },
          negativeClickListener = closeClickListener(error.api, errorType)
        ).run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
      is ApiError.ErrorType.RetryScreen,
      ApiError.ErrorType.AutoRetry -> {
        retryListener?.invoke(error, errorType) ?: error.retry?.invoke()
      }
      is ApiError.ErrorType.BackToLogin -> {
        Dialog.createCloseDialog(
          title = errorType.title,
          message = errorType.message,
          positiveClickListener = DialogInterface.OnClickListener { _, _ -> forceClose(context) }
        ).run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
      is ApiError.ErrorType.Validation -> {
        val validationMap = HashMap<String, String>().apply {
          put("email", "Lỗi format")
        }
        // mall関連などでuserが基本目にすることのないエラー
        val hideFieldList = arrayListOf("abc", "cde")

        val errorMessageList = ArrayList<String>()
        errorType.errors.forEach {
          it.field?.let { field ->
            validationMap[field]?.let { key -> errorMessageList.add(key) }
          }
        }

        val bottomMessage = if (errorType.unnecessaryCorrectly) {
          "Vui lòng nhập."
        } else {
          "Vui lòng nhập chính xác."
        }

        val message =
          errorMessageList.distinct().filter {
            if (BuildConfig.DEBUG) true else !hideFieldList.contains(it)
          }.joinToString(separator = "、")
        if (message.isNotEmpty()) {
          Timber.d(message)
          Dialog.createCloseDialog(
            message = message + bottomMessage,
            positiveClickListener = closeClickListener(error.api, errorType)
          ).run {
            setOnShowListener(showListener)
            setOnDismissListener(dismissListener)
            show(fragmentManager)
          }
        }
        errorResultListener?.invoke(ApiError.Result(error.api, errorType))
      }
      is ApiError.ErrorType.Unknown -> {
        Dialog.createCloseDialog(
          message = "Xin lỗi đã làm phiền bạn, nhưng vui lòng thử lại sau.",
          positiveClickListener = closeClickListener(error.api, errorType)
        ).run {
          setOnShowListener(showListener)
          setOnDismissListener(dismissListener)
          show(fragmentManager)
        }
      }
    }
  }

  private fun getErrorType(
    error: Error,
    context: Context,
    targetClassName: String? = null
  ): ApiError.ErrorType {
    val isHomeTab = isHomeTab(error.api, targetClassName)

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

  private fun isHomeTab(api: ApiEnum, targetClassName: String?): Boolean {
    if(targetClassName.isNullOrEmpty()) return false
    return when(targetClassName) {
      ApiError.getTargetClassName<MainFragment>() -> {
        true
      }
      else -> false
    }
  }

  private fun closeClickListener(api: ApiEnum, errorType: ApiError.ErrorType): DialogInterface.OnClickListener {
    return DialogInterface.OnClickListener { _, _ ->
      closeClickListener?.invoke(api, errorType)
    }
  }

  private fun retryClickListener(error: Error, errorType: ApiError.ErrorType): DialogInterface.OnClickListener {
    return DialogInterface.OnClickListener { _, _ ->
      retryListener?.invoke(error, errorType) ?: error.retry?.invoke()
    }
  }

  private fun forceClose(context: Context) {
    context.run {
      Application.instance.logout()
//      val intent = Intent(this, LoginActivity::class.java)
//      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//      startActivity(intent)
    }
  }

}
