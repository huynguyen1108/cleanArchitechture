package com.example.basecleanarchitechturedagger.data.remote.entity.error

import androidx.annotation.Keep

@Keep
data class AppServerError(
  override var status: Int,
  val message: String? = null,
  val errors: ArrayList<Error>? = null,
  val code: String? = null
) : ErrorInterface {
  val primaryErrorField: String?
    get() = errors?.getOrNull(0)?.field

  val primaryFailureCode: String?
    get() = code ?: errors?.getOrNull(0)?.code

  fun hasErrors(): Boolean {
    return !errors.isNullOrEmpty()
  }

  @Keep
  class Error {
    private val resource: String? = null
    val field: String? = null
    val code: String? = null

    override fun toString(): String {
      return "Error(resource='$resource', field='$field', code='$code')"
    }
  }
}
