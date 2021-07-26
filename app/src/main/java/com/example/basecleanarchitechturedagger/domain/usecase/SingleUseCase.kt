package com.example.basecleanarchitechturedagger.domain.usecase

import com.example.basecleanarchitechturedagger.domain.app.SchedulerProviderInterface
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber

abstract class SingleUseCase<T, in Params> constructor(
  private val schedulerProvider: SchedulerProviderInterface
) {

  private val disposables = CompositeDisposable()

  protected abstract fun buildUseCaseObservable(params: Params) : Single<T>

  open fun execute(singleObserver: DisposableSingleObserver<T>, params: Params) {
    val single = buildUseCaseObservable(params)
      .doOnError { Timber.e(it) }
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui()) as Single<T>
    addDisposable(single.subscribeWith(singleObserver))
  }

  /**
   * Dispose from current [CompositeDisposable].
   */
  fun disposable() {
    if(!disposables.isDisposed) disposables.dispose()
  }

  /**
   * Dispose from current [CompositeDisposable].
   */
  private fun addDisposable(disposable: Disposable) {
    disposables.add(disposable)
  }
}
