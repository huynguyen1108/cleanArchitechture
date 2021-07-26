package com.example.basecleanarchitechturedagger.presentation.di.modules

import com.example.basecleanarchitechturedagger.presentation.di.subcomponent.MainActivityModule
import com.example.basecleanarchitechturedagger.presentation.ui.main.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

  @ContributesAndroidInjector(modules = [MainActivityModule::class])
  abstract fun contributesMainActivity(): MainActivity
}
