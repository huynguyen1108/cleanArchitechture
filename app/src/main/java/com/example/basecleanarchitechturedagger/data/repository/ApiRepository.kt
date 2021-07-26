package com.example.basecleanarchitechturedagger.data.repository

import com.example.basecleanarchitechturedagger.data.remote.Api
import com.example.basecleanarchitechturedagger.data.remote.ApiEnum
import com.example.basecleanarchitechturedagger.data.remote.entity.AN0001Request
import com.example.basecleanarchitechturedagger.data.remote.entity.wrapper.Error
import com.example.basecleanarchitechturedagger.domain.entity.MovieResponse
import com.example.basecleanarchitechturedagger.domain.entity.UpComingRequest
import com.example.basecleanarchitechturedagger.domain.repository.ApiRepositoryInterface
import io.reactivex.Single
import javax.inject.Inject

class ApiRepository @Inject constructor(
  private val api: Api
) : ApiRepositoryInterface{

  override fun callApiUpComing(request: UpComingRequest): Single<MovieResponse> {
    return api.callApiUpComing(AN0001Request.fromEntity(request))
      .map {
        it.toEntity()
      }
      .onErrorResumeNext {
        Single.error(Error(ApiEnum.AN0001, it))
      }
  }
}
