package com.example.basecleanarchitechturedagger.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.example.basecleanarchitechturedagger.data.remote.ApiEnum
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import io.reactivex.*
import java.util.concurrent.TimeUnit

fun <T> withDelay(
  scheduler: Scheduler,
  duration: Long,
  timeUnit: TimeUnit,
  single: () -> Single<T>
): Single<T> = Single.just(Unit)
  .delay(duration, timeUnit, scheduler)
  .flatMap { single.invoke() }

fun <T : Throwable> Observable<T>.fallBackToUnclassifiedError(): Observable<Error> = map {
  if (it is Error) it else Error(ApiEnum.UnClassified, it)
}

fun <T> Flowable<T>.toLiveData(): LiveData<T> {
  return LiveDataReactiveStreams.fromPublisher(this)
}

fun <T> Observable<T>.toLiveData(backPressureStrategy: BackpressureStrategy): LiveData<T> {
  return LiveDataReactiveStreams.fromPublisher(this.toFlowable(backPressureStrategy))
}

fun <T> Single<T>.toLiveData(): LiveData<T> {
  return LiveDataReactiveStreams.fromPublisher(this.toFlowable())
}

fun <T> Maybe<T>.toLiveData(): LiveData<T> {
  return LiveDataReactiveStreams.fromPublisher(this.toFlowable())
}

fun <T> Completable.toLiveData(): LiveData<T> {
  return LiveDataReactiveStreams.fromPublisher(this.toFlowable())
}

fun processingToLiveData(vararg processings: Observable<Boolean>): LiveData<Boolean> {
  return Observable
    .combineLatest(processings.asList()) {
      it.reduce { lhs, rhs -> (lhs as Boolean) || (rhs as Boolean) } as Boolean
    }
    .toLiveData(BackpressureStrategy.LATEST)
}
