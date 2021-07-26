package com.example.basecleanarchitechturedagger.presentation

import com.example.basecleanarchitechturedagger.domain.app.SchedulerProviderInterface
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SchedulerProvider : SchedulerProviderInterface {
  override fun computation(): Scheduler = Schedulers.computation()

  override fun io(): Scheduler = Schedulers.io()

  override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}

class ImmediateSchedulerProvider: SchedulerProviderInterface {
  override fun computation(): Scheduler = Schedulers.trampoline()

  override fun io(): Scheduler = Schedulers.trampoline()

  override fun ui(): Scheduler = Schedulers.trampoline()

}
