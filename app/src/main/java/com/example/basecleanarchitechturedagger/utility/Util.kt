package com.example.basecleanarchitechturedagger.utility

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber

class Util {
  companion object {
    @SuppressLint("NewApi")
    fun isNetworkConnected(context: Context): Boolean {
      val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val linkProperty = cm.getLinkProperties(cm.activeNetwork)
      Timber.e("$linkProperty")
      return linkProperty != null
    }
  }
}
