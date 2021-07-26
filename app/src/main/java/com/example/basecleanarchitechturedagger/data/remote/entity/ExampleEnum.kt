package com.example.basecleanarchitechturedagger.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
enum class ExampleEnum {
  @Json(name = "1")
  EXAMPLE_1,
  @Json(name = "2")
  EXAMPLE_2,
}
