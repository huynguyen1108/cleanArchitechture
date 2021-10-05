package com.example.basecleanarchitechturedagger.data.remote.entity.error

class LocalException(val status: LocalExceptionStatus, val title: String?, message: String?) : Exception(message)

enum class LocalExceptionStatus(val value: Int) {
  INVALID_QR_CODE(1003);
}
