package com.example.basecleanarchitechturedagger.presentation.di.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory<VM : ViewModel> @Inject constructor(
  private val viewModel: Provider<VM>
) : ViewModelProvider.Factory {

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return viewModel.get() as T
  }
}
