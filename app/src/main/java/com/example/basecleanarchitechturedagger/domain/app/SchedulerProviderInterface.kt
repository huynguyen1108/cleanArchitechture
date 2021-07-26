package com.example.basecleanarchitechturedagger.domain.app

import io.reactivex.Scheduler

interface SchedulerProviderInterface {
  fun computation(): Scheduler
  fun io(): Scheduler
  fun ui(): Scheduler
}
