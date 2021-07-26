package com.example.basecleanarchitechturedagger.presentation.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import com.example.basecleanarchitechturedagger.data.usecase.UpComingUseCase
import com.example.basecleanarchitechturedagger.domain.entity.MovieResponse
import com.example.basecleanarchitechturedagger.domain.entity.UpComingRequest
import com.example.basecleanarchitechturedagger.domain.usecase.SingleUseCaseObserver
import com.example.basecleanarchitechturedagger.extension.fallBackToUnclassifiedError
import com.example.basecleanarchitechturedagger.extension.processingToLiveData
import com.example.basecleanarchitechturedagger.presentation.ui.base.BaseViewModel
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class MainViewModel @Inject constructor(
  private val getUpComingUseCase: UpComingUseCase
) : BaseViewModel() {

  private val _movie = MutableLiveData<MovieResponse>()
  val movie: LiveData<MovieResponse> = _movie

  private val _failed = MutableLiveData<Error>()
  val failed: LiveData<Error> = _failed

  private val getUpComingObserver = SingleUseCaseObserver<MovieResponse>()

  val progress: LiveData<Boolean> = processingToLiveData(
    getUpComingObserver.processing.startWith(false)
  )

  init {
    getUpComingObserver.apply {
      succeeded.subscribe {
        _movie.value = it
      }.addTo(compositeDispose)

      failed.fallBackToUnclassifiedError().subscribe {
        _failed.value = it
      }.addTo(compositeDispose)
    }
  }

  fun getUpComing() {
    getUpComingObserver.invokeUseCase(
      getUpComingUseCase,
      UpComingRequest(
        apiKey = "",
        language = ""
      )
    )
  }

  override fun onCleared() {
    super.onCleared()
    getUpComingUseCase.disposable()
  }
}
