package com.example.basecleanarchitechturedagger.data.remote.entity.wrapper

import com.example.basecleanarchitechturedagger.data.remote.ApiEnum
import java.lang.RuntimeException

typealias Retry = () -> Unit

data class Error(val api: ApiEnum, val throwable: Throwable) : RuntimeException(throwable) {
  var retry: Retry? = null
}
