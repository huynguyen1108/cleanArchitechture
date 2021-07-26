package com.example.basecleanarchitechturedagger

import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.presentation.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class Application : DaggerApplication() {
  companion object {
    lateinit var instance: Application private set
  }

  @Inject
  private lateinit var appSettingRepositoryInterface: AppSettingRepositoryInterface

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent
      .builder()
      .application(this)
      .build()
  }

  override fun onCreate() {
    super.onCreate()
    initializeTimber()
    instance = this
  }

  private fun initializeTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(object : Timber.DebugTree() {
        private var stackTraceElement: StackTraceElement? = null

        override fun createStackElementTag(element: StackTraceElement): String? {
          stackTraceElement = element
          return "App:${super.createStackElementTag(element)}#${element.methodName}"
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
          val caller = stackTraceElement?.let { "(${it.fileName}:${it.lineNumber}" } ?: ""
          super.log(priority, tag, "$message$caller", t)
        }
      })
    }
  }

  fun logout() {
    appSettingRepositoryInterface.run {
      clearAccessToken()
    }
  }

}
