package com.example.basecleanarchitechturedagger.presentation.di

import com.example.basecleanarchitechturedagger.Application
import com.example.basecleanarchitechturedagger.presentation.di.modules.ActivityModule
import com.example.basecleanarchitechturedagger.presentation.di.modules.AppModule
import com.example.basecleanarchitechturedagger.presentation.di.modules.FragmentModule
import com.example.basecleanarchitechturedagger.presentation.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    ActivityModule::class,
    FragmentModule::class
  ]
)
interface AppComponent : AndroidInjector<Application> {
  @Component.Builder
  interface Builder {
    fun application(@BindsInstance application: Application) : Builder
    fun build() : AppComponent
  }
}
