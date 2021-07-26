package com.example.basecleanarchitechturedagger.domain.repository

import com.example.basecleanarchitechturedagger.domain.entity.MovieResponse
import com.example.basecleanarchitechturedagger.domain.entity.UpComingRequest
import io.reactivex.Single

interface ApiRepositoryInterface {
  fun callApiUpComing(request: UpComingRequest) : Single<MovieResponse>
}
