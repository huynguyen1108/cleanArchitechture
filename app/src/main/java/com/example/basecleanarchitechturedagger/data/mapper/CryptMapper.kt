package com.example.basecleanarchitechturedagger.data.mapper

import android.content.Context
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class CryptMapper @Inject constructor(
  private val context: Context
) : CryptMapperInterface {

  companion object {
    private const val AES = "AES"

    init {
      System.loadLibrary("key")
    }

    @JvmStatic
    external fun getKey(context: Context): String
  }

  override fun encrypt(value: String): String {
    val originalBytes = value.toByteArray()
    val secretKeySpec = getSecretKeySpec()
    val cipher = Cipher.getInstance(AES)
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    return Base64.encodeToString(cipher.doFinal(originalBytes), 0)
  }

  override fun decrypt(value: String): String {
    val encryptBytes = Base64.decode(value, 0)
    val secretKeySpec = getSecretKeySpec()
    val cipher = Cipher.getInstance(AES)
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    return String(cipher.doFinal(encryptBytes))
  }

  private fun getSecretKeySpec(): SecretKeySpec {
    val uuidKey = "UUID"
    val u = context.getSharedPreferences(
      context.packageName,
      AppCompatActivity.MODE_PRIVATE
    ).run {
      var uuidValue = getString(uuidKey, "")!!
      if (!contains(uuidKey) || uuidKey.isEmpty()) {
        uuidValue = UUID.randomUUID().toString()
        edit().putString(uuidKey, uuidValue)
      }
      uuidValue.substring(24)
    }

    val k = getKey(context) + u
    val secretKeySpec = k.toByteArray()
    return SecretKeySpec(secretKeySpec, AES)
  }
}
