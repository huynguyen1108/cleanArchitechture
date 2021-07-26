package com.example.basecleanarchitechturedagger.domain.entity

import com.squareup.moshi.Json

data class MovieResponse(
  var popularity: Double,
  var voteCount: Int,
  var video: Boolean,
  var posterPath: String,
  var id: Int,
  var adult: Boolean,
  var backdropPath: String,
  var originalLanguage: String,
  var originalTitle: String,
  var title: String,
  var voteAverage: Double,
  var overview: String,
  var name: String,
  var releaseDate: String
)
