package com.example.basecleanarchitechturedagger.domain.usecase

import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Retry
import io.reactivex.Observable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class CompleteUseCaseObserver {
  private val _processing = BehaviorSubject.create<Boolean>()
  val processing: Observable<Boolean> = _processing

  private val _completed = PublishSubject.create<Unit>()
  val completed: Observable<Unit> = _completed

  private val _failed = PublishSubject.create<Throwable>()
  val failed: Observable<Throwable> = _failed

  private fun completableObserverIfNotPerformed(retry: Retry?) : DisposableCompletableObserver? {
    if(_processing.value == true) {
      _failed.onNext(ActionAlreadyPerformingException())
      return null
    }

    _processing.onNext(true)
    return object : DisposableCompletableObserver() {
      override fun onComplete() {
        _processing.onNext(false)
        _completed.onNext(Unit)
      }

      override fun onError(e: Throwable) {
        _processing.onNext(false)
        _failed.onNext(e.also { (it as? Error)?.retry = retry })
      }
    }
  }

  private fun completableObserve(retry: Retry?) : DisposableCompletableObserver? {
    return object : DisposableCompletableObserver() {
      override fun onComplete() {
        _completed.onNext(Unit)
      }

      override fun onError(e: Throwable) {
        _failed.onNext(e.also { (it as? Error)?.retry = retry })
      }
    }
  }


}
