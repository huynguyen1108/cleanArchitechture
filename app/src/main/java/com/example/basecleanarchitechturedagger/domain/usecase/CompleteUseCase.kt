package com.example.basecleanarchitechturedagger.domain.usecase

import com.example.basecleanarchitechturedagger.domain.app.SchedulerProviderInterface
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver

abstract class CompleteUseCase<in Params> protected constructor(
  private val schedulerProvider: SchedulerProviderInterface
) {

  private val disposables = CompositeDisposable()

  protected abstract fun buildUseCaseObservable(params: Params): Completable

  fun execute(completableObserver: DisposableCompletableObserver, params: Params) {
    val completable = buildUseCaseObservable(params)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
    addDisposable(completable.subscribeWith(completableObserver))
  }

  /**
   * Dispose from current [CompositeDisposable].
   */
  fun dispose() {
    if (!disposables.isDisposed) disposables.dispose()
  }

  /**
   * Dispose from current [CompositeDisposable].
   */
  private fun addDisposable(disposable: Disposable) {
    disposables.add(disposable)
  }
}
