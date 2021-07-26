package com.example.basecleanarchitechturedagger.presentation.di.modules

import com.example.basecleanarchitechturedagger.presentation.ui.example.fragment.ExampleFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
  @ContributesAndroidInjector
  abstract fun contributesExampleFragment(): ExampleFragment
}
