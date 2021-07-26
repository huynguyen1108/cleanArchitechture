package com.example.basecleanarchitechturedagger.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.view.ContextMenu
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.example.basecleanarchitechturedagger.data.mapper.CryptMapperInterface
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface

class AppSettingRepository constructor(
  private val context: Context,
  private val mapper: CryptMapperInterface
) : AppSettingRepositoryInterface {
  @Keep
  enum class Key(val needCrypt: Boolean = false) {
    ACCESS_TOKEN(true)
  }

  private val prefs: SharedPreferences by lazy {
    context.getSharedPreferences(context.packageName, AppCompatActivity.MODE_PRIVATE)
  }

  private fun isSaved(key: Key): Boolean = prefs.contains(key.name)

  private fun loadString(key: Key, defaultValue: String?): String? {
    return if (isSaved(key)) {
      prefs.getString(key.name, defaultValue)?.let {
        if(key.needCrypt) mapper.decrypt(it) else it
      }
    } else {
      defaultValue
    }
  }

  private fun saveString(key: Key, value: String?) {
    val saveValue = value?.let {
      if(key.needCrypt) mapper.encrypt(it) else it
    }
    prefs.edit()
      .putString(key.name, saveValue)
      .apply()
  }

  private fun loadInt(key: Key, defaultValue: Int): Int {
    return prefs.getInt(key.name, defaultValue)
  }

  private fun saveInt(key: Key, value: Int) {
    prefs.edit().putInt(key.name, value).apply()
  }

  private fun loadLong(key: Key, defaultValue: Long): Long {
    return prefs.getLong(key.name, defaultValue)
  }

  private fun saveLong(key: Key, value: Long) {
    prefs.edit().putLong(key.name, value).apply()
  }

  private fun loadBoolean(key: Key, defaultValue: Boolean): Boolean {
    return prefs.getBoolean(key.name, defaultValue)
  }

  private fun saveBoolean(key: Key, value: Boolean) {
    prefs.edit().putBoolean(key.name, value).apply()
  }

  private fun contains(key: Key): Boolean {
    return prefs.contains(key.name)
  }

  private fun remove(key: Key) {
    prefs.edit()
      .remove(key.name)
      .apply()
  }

  override fun getAccessToken(): String? {
    return loadString(Key.ACCESS_TOKEN, "")
  }

  override fun pushAccessToken(token: String?) {
    saveString(Key.ACCESS_TOKEN, token)
  }

  override fun clearAccessToken() {
    saveString(Key.ACCESS_TOKEN, null)
  }
}
