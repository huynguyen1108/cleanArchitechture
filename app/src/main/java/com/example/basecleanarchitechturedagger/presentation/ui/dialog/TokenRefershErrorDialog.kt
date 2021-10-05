package com.example.basecleanarchitechturedagger.presentation.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.example.basecleanarchitechturedagger.Application
import com.example.basecleanarchitechturedagger.data.remote.entity.TokenAuthResponse
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TokenRefreshErrorDialog @Inject constructor(
  private val appSettingRepository: AppSettingRepositoryInterface,
  private val tokenRepository: TokenRepositoryInterface
) {

  interface RetryListener {
    fun onSuccess()
  }

  fun createRefreshTokenForceLogoutDialog(context: Context?): Dialog? {
    Application.instance.logout()
    return context?.run {
      Dialog.Builder()
        .title("Xử lý không thành công")
        .message("Xin lỗi đã làm phiền bạn, nhưng vui lòng đăng nhập lại")
        .positiveLabel("Thoát")
        .positiveListener(
          DialogInterface.OnClickListener { _, _ ->
//            startActivity(
//              Intent(this, LoginActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//              }
//            )
          }
        )
        .build()
    }
  }

  fun createTokenRefreshErrorDialog(
    context: Context?,
    errorStatus: Int,
    fragmentManager: FragmentManager,
    listener: RetryListener
  ): Dialog? {
    return context?.run {
      val builder = Dialog.Builder()
        .positiveLabel("Thử lại")
        .positiveListener(
          DialogInterface.OnClickListener { _, _ ->
            requestTokenRefresh(context, fragmentManager, listener, errorStatus)
          }
        )
        .negativeLabel("Thoát")
        .negativeListener(
          DialogInterface.OnClickListener { _, _ ->
            showLogoutConfirmationDialog(context, fragmentManager, listener)
          }
        )
      when (errorStatus) {
        408 -> {
          builder.title("Không đọc được")
          builder.message("Xin lỗi đã làm phiền bạn, nhưng vui lòng thử lại")
        }
        429 -> {
          builder.title("Không đọc được")
          builder.message("Truy cập tập trung. Xin lỗi đã làm phiền bạn, nhưng vui lòng thử lại sau.")
        }
        503 -> {
          builder.title("Đang bảo trì")
          builder.message("Xin lỗi đã làm phiền bạn, nhưng vui lòng thử lại sau.")
        }
      }
      builder.build()
    }
  }

  private fun showLogoutConfirmationDialog(
    context: Context?,
    fragmentManager: FragmentManager,
    listener: RetryListener
  ) {
    context?.run {
      Dialog.Builder()
        .message("Bạn có chắc chắn bạn muốn thoát?")
        .positiveLabel("Ok")
        .positiveListener(
          DialogInterface.OnClickListener { _, _ ->
            Application.instance.logout()
//            startActivity(
//              Intent(this, LoginActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//              }
//            )
          }
        )
        .negativeLabel("Hủy bỏ")
        .negativeListener(
          DialogInterface.OnClickListener { _, _ ->
            showRetryConfirmationDialog(context, fragmentManager, listener)
          }
        )
        .build()
        .show(fragmentManager, this@TokenRefreshErrorDialog::class.java.name)
    }
  }

  private fun showRetryConfirmationDialog(
    context: Context?,
    fragmentManager: FragmentManager,
    listener: RetryListener
  ) {
    context?.run {
      Dialog.Builder()
        .message("Bạn có muốn thử lại không?")
        .positiveLabel("Thử lại")
        .positiveListener(
          DialogInterface.OnClickListener { _, _ ->
            requestTokenRefresh(context, fragmentManager, listener)
          }
        )
        .negativeLabel("Thoát")
        .negativeListener(
          DialogInterface.OnClickListener { _, _ ->
            showLogoutConfirmationDialog(context, fragmentManager, listener)
          }
        )
        .build()
        .show(fragmentManager, this@TokenRefreshErrorDialog::class.java.name)
    }
  }

  private fun requestTokenRefresh(
    context: Context,
    fragmentManager: FragmentManager,
    listener: RetryListener,
    status: Int = 0
  ) {
    val progress = LoadingProgress()
    progress.showLoadingProgress(fragmentManager)

    tokenRepository.refresh(appSettingRepository.getAccessToken())
      .enqueue(object : Callback<TokenAuthResponse> {
        override fun onFailure(call: Call<TokenAuthResponse>, t: Throwable) {
          progress.hideLoadingProgress()
          when (status) {
            408, 429, 503 -> {
              createTokenRefreshErrorDialog(
                context,
                status,
                fragmentManager,
                listener
              )?.show(fragmentManager, this@TokenRefreshErrorDialog::class.java.name)
            }
            else -> {
              showRetryConfirmationDialog(context, fragmentManager, listener)
            }
          }
        }

        override fun onResponse(call: Call<TokenAuthResponse>, response: Response<TokenAuthResponse>) {
          progress.hideLoadingProgress()
          if (response.code() == 200 or 201 or 204) {
            appSettingRepository.pushAccessToken(response.body()!!.accessToken)
            listener.onSuccess()
          } else {
            if (status == 0) {
              showRetryConfirmationDialog(context, fragmentManager, listener)
            } else {
              when (response.code()) {
                408, 429, 503 -> {
                  createTokenRefreshErrorDialog(
                    context,
                    status,
                    fragmentManager,
                    listener
                  )?.show(fragmentManager, this@TokenRefreshErrorDialog::class.java.name)
                }
              }
            }
          }
        }
      })
  }
}
