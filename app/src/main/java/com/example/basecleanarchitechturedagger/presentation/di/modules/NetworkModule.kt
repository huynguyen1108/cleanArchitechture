package com.example.basecleanarchitechturedagger.presentation.di.modules

import android.content.Context
import com.example.basecleanarchitechturedagger.BuildConfig
import com.example.basecleanarchitechturedagger.data.remote.*
import com.example.basecleanarchitechturedagger.data.remote.entity.ExampleEnum
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface
import com.example.basecleanarchitechturedagger.presentation.di.annotation.AppContext
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {

  private fun createBaseHttpClientBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder()
      .addNetworkInterceptor(StethoInterceptor())
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .retryOnConnectionFailure(false)
  }

  private fun createMoshiBuilder(): Moshi {
    val moshi = Moshi.Builder()

    // add String Enum value adapters
    moshi.add(
      ExampleEnum::class.java,
      EnumJsonAdapter
        .create(ExampleEnum::class.java)
        .withUnknownFallback(ExampleEnum.EXAMPLE_1)
    )

    moshi.add(KotlinJsonAdapterFactory())
    return moshi.build()
  }

  @Singleton
  @Named("BaseApi")
  @Provides
  fun provideBaseApiOkHttpClient(
    @AppContext context: Context,
    appSettingRepository: AppSettingRepositoryInterface,
    tokenRepository: TokenRepositoryInterface
  ): OkHttpClient {
    return createBaseHttpClientBuilder()
      .addInterceptor(
        Interceptor(appSettingRepository = appSettingRepository)
      )
      .authenticator(
        TokenAuthenticator(
          tokenRepository = tokenRepository,
          appSettingRepository = appSettingRepository
        )
      )
      .build()
  }

  @Singleton
  @Provides
  fun provideBaseApi(
    @AppContext context: Context,
    @Named("BaseApi") client: OkHttpClient
  ): Api {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.API_BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create(createMoshiBuilder()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(client)
      .build()
      .create(Api::class.java)
  }

  @Singleton
  @Named("RefreshToken")
  @Provides
  fun provideRefreshApiOkHttpClient(
    @AppContext context: Context,
    appSettingsRepository: AppSettingRepositoryInterface
  ): OkHttpClient {
    return createBaseHttpClientBuilder()
      .addInterceptor(
        RefreshTokenAuthInterceptor(
          appSettingRepository = appSettingsRepository
        )
      )
      .build()
  }

  @Singleton
  @Provides
  fun provideRefreshApi(
    @AppContext context: Context,
    @Named("RefreshToken") client: OkHttpClient
  ): RefreshTokenApi {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.API_BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create(createMoshiBuilder()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(client)
      .build()
      .create(RefreshTokenApi::class.java)
  }
}
