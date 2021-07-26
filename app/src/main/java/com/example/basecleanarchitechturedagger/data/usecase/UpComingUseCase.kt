package com.example.basecleanarchitechturedagger.data.usecase

import com.example.basecleanarchitechturedagger.domain.app.SchedulerProviderInterface
import com.example.basecleanarchitechturedagger.domain.entity.MovieResponse
import com.example.basecleanarchitechturedagger.domain.entity.UpComingRequest
import com.example.basecleanarchitechturedagger.domain.repository.ApiRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.usecase.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class UpComingUseCase @Inject constructor(
  private val apiRepository: ApiRepositoryInterface,
  schedulerProvider: SchedulerProviderInterface
) : SingleUseCase<MovieResponse, UpComingRequest>(schedulerProvider){
  override fun buildUseCaseObservable(params: UpComingRequest): Single<MovieResponse> {
    return apiRepository.callApiUpComing(params)
  }
}
