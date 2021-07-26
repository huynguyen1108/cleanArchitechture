package com.example.basecleanarchitechturedagger.presentation.di.subcomponent

import androidx.appcompat.app.AppCompatActivity
import com.example.basecleanarchitechturedagger.presentation.ui.main.activity.MainActivity
import com.example.basecleanarchitechturedagger.presentation.ui.main.dialog.MainDialogFragment
import com.example.basecleanarchitechturedagger.presentation.ui.main.fragment.MainFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
  @Binds
  abstract fun bindAppCompatActivity(mainActivity: MainActivity): AppCompatActivity

  @ContributesAndroidInjector
  abstract fun contributesMainFragment(): MainFragment

  @ContributesAndroidInjector
  abstract fun contributesMainDialogFragment(): MainDialogFragment
}
