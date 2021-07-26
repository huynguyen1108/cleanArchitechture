package com.example.basecleanarchitechturedagger.domain.usecase

import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Retry
import io.reactivex.Observable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject


class ActionAlreadyPerformingException : Throwable() {
  override val message: String?
    get() = "The action to execute is performing now"
}

open class SingleUseCaseMappableObserver<SourceType, ReturnType> {
  // note: `BehaviorSubject` emits previous state on subscribe
  private val _processing = BehaviorSubject.create<Boolean>()
  val processing: Observable<Boolean> = _processing

  private val _succeeded = PublishSubject.create<ReturnType>()
  val succeeded: Observable<ReturnType> = _succeeded

  private val _failed = PublishSubject.create<Throwable>()
  val failed: Observable<Throwable> = _failed

  // / used when you don't want to observe at multiple points from one usecase.
  fun singleObserverIfNotPreformed(
    retry: Retry? = null,
    mapper: (SourceType) -> ReturnType
  ): DisposableSingleObserver<SourceType>? {
    if (_processing.value == true) {
      _failed.onNext(ActionAlreadyPerformingException())
      return null
    }

    _processing.onNext(true)

    return object : DisposableSingleObserver<SourceType>() {
      override fun onSuccess(t: SourceType) {
        _processing.onNext(false)
        _succeeded.onNext(mapper(t))
      }

      override fun onError(e: Throwable) {
        _processing.onNext(false)
        _failed.onNext(e.also { (it as? Error)?.retry = retry })
      }
    }
  }

  // / used when you want to observe simple single observables
  fun singleObserver(retry: Retry?, mapper: (SourceType) -> ReturnType): DisposableSingleObserver<SourceType> {
    return object : DisposableSingleObserver<SourceType>() {
      override fun onSuccess(t: SourceType) {
        _succeeded.onNext(mapper(t))
      }

      override fun onError(e: Throwable) {
        _failed.onNext(e.also { (it as? Error)?.retry = retry })
      }

    }
  }
}
