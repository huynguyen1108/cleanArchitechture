package com.example.basecleanarchitechturedagger.data.remote.entity

import com.example.basecleanarchitechturedagger.domain.entity.MovieResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AN0001Response(
  var popularity: Double,
  @Json(name = "vote_count")
  var voteCount: Int,
  var video: Boolean,
  @Json(name = "poster_path")
  var posterPath: String,
  var id: Int,
  var adult: Boolean,
  @Json(name = "backdrop_path")
  var backdropPath: String,
  @Json(name = "original_language")
  var originalLanguage: String,
  @Json(name = "original_title")
  var originalTitle: String,
  var title: String,
  @Json(name = "vote_average")
  var voteAverage: Double,
  var overview: String,
  var name: String,
  @Json(name = "release_date")
  var releaseDate: String
) {
  fun toEntity(): MovieResponse {
    return MovieResponse(
      popularity = popularity,
      voteCount = voteCount,
      video = video,
      posterPath = posterPath,
      id = id,
      adult = adult,
      backdropPath = backdropPath,
      originalLanguage = originalLanguage,
      originalTitle = originalTitle,
      title = title,
      voteAverage = voteAverage,
      overview = overview,
      name = name,
      releaseDate = releaseDate
    )
  }
}
