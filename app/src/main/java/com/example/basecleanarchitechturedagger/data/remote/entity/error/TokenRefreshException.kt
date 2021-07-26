package com.example.basecleanarchitechturedagger.data.remote.entity.error

import retrofit2.HttpException
import retrofit2.Response

class TokenRefreshException(response: Response<*>) : HttpException(response) {
}
