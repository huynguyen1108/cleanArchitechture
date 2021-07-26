package com.example.basecleanarchitechturedagger.presentation.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
  protected val compositeDispose = CompositeDisposable()

  override fun onCleared() {
    compositeDispose.dispose()
  }
}
