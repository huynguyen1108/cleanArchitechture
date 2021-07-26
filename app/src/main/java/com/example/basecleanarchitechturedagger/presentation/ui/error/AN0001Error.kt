package com.example.basecleanarchitechturedagger.presentation.ui.error

import android.content.Context
import com.example.basecleanarchitechturedagger.data.remote.ApiEnum
import com.example.basecleanarchitechturedagger.data.remote.entity.error.AppServerError
import com.example.basecleanarchitechturedagger.data.remote.entity.error.ErrorInterface

class AN0001Error(targetClassName: String) : ApiError(ApiEnum.AN0001, targetClassName) {
  override fun getErrorType(context: Context, error: ErrorInterface): ErrorType {
    return when ((error as? AppServerError)?.status) {
      400 -> {
        ErrorType.JustClose(
          title = "Lỗi",
          message = "Lỗi"
        )
      }
      else -> {
        ErrorType.Unknown
      }
    }
  }
}
