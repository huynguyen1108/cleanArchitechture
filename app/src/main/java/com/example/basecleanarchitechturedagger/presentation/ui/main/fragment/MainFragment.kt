package com.example.basecleanarchitechturedagger.presentation.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.basecleanarchitechturedagger.R
import com.example.basecleanarchitechturedagger.databinding.FragmentMainBinding
import com.example.basecleanarchitechturedagger.presentation.di.viewmodelfactory.ViewModelFactory
import com.example.basecleanarchitechturedagger.presentation.ui.dialog.LoadingProgress
import com.example.basecleanarchitechturedagger.presentation.ui.error.ApiErrorHandler
import com.example.basecleanarchitechturedagger.presentation.ui.main.viewmodel.MainViewModel
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MainFragment : DaggerFragment() {
  @Inject
  lateinit var viewModelFactory: ViewModelFactory<MainViewModel>

  private val viewModel: MainViewModel by viewModels { viewModelFactory }

  private val binding: FragmentMainBinding by dataBinding()

  private val progress: LoadingProgress by lazy {
    LoadingProgress()
  }

  @Inject
  lateinit var apiErrorHandler: ApiErrorHandler

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.lifecycleOwner = viewLifecycleOwner

    observer()

    viewModel.getUpComing()
  }

  private fun observer() {
    viewModel.progress.observe(
      viewLifecycleOwner,
      Observer {
        if(it) progress.showLoadingProgress(this) else progress.hideLoadingProgress()
      }
    )

    viewModel.failed.observe(
      viewLifecycleOwner,
      Observer {
        apiErrorHandler.show(this, it)
      }
    )

    viewModel.movie.observe(
      viewLifecycleOwner,
      Observer {

      }
    )
  }
}
