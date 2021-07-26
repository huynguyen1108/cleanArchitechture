package com.example.basecleanarchitechturedagger.domain.usecase

import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Retry
import io.reactivex.Observable
import io.reactivex.observers.DisposableSingleObserver

open class SingleUseCaseObserver<T> {
  private val core: SingleUseCaseMappableObserver<T, T> = SingleUseCaseMappableObserver()
  val processing: Observable<Boolean> = core.processing
  val succeeded: Observable<T> = core.succeeded
  val failed: Observable<Throwable> = core.failed

  private fun singleObserverIfNotPerformed(retry: Retry?): DisposableSingleObserver<T>? =
    core.singleObserverIfNotPreformed(retry) { it }

  private fun singleObserver(retry: Retry?): DisposableSingleObserver<T>? = core.singleObserver(retry) { it }

  fun <TArg> invokeUseCase(
    useCase: SingleUseCase<T, TArg>,
    params: TArg,
    retry: Retry? = { invokeUseCase(useCase, params) }
  ) {
    singleObserverIfNotPerformed(retry)?.let { useCase.execute(it, params) }
  }
}
