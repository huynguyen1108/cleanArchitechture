package com.example.basecleanarchitechturedagger.data.mapper

interface CryptMapperInterface {
  fun encrypt(value: String): String
  fun decrypt(value: String): String
}
