package com.example.basecleanarchitechturedagger.presentation.di.modules

import android.content.Context
import com.example.basecleanarchitechturedagger.Application
import com.example.basecleanarchitechturedagger.data.mapper.CryptMapper
import com.example.basecleanarchitechturedagger.data.mapper.CryptMapperInterface
import com.example.basecleanarchitechturedagger.data.remote.Api
import com.example.basecleanarchitechturedagger.data.remote.RefreshTokenApi
import com.example.basecleanarchitechturedagger.data.repository.ApiRepository
import com.example.basecleanarchitechturedagger.data.repository.AppSettingRepository
import com.example.basecleanarchitechturedagger.data.repository.TokenRepository
import com.example.basecleanarchitechturedagger.domain.app.SchedulerProviderInterface
import com.example.basecleanarchitechturedagger.domain.repository.ApiRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface
import com.example.basecleanarchitechturedagger.presentation.SchedulerProvider
import com.example.basecleanarchitechturedagger.presentation.di.annotation.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
  @Singleton
  @Provides
  @AppContext
  fun provideContext(application: Application): Context = application.applicationContext

  @Singleton
  @Provides
  fun provideTokenRepositoryInterface(
    api: RefreshTokenApi
  ): TokenRepositoryInterface {
    return TokenRepository(
      api = api
    )
  }

  @Singleton
  @Provides
  fun provideCryptMapperInterface(
    @AppContext context: Context
  ): CryptMapperInterface {
    return CryptMapper(context = context)
  }

  @Singleton
  @Provides
  fun provideAppSettingRepositoryInterface(
    @AppContext context: Context,
    mapper: CryptMapperInterface
  ): AppSettingRepositoryInterface {
    return AppSettingRepository(
      context = context,
      mapper = mapper
    )
  }

  @Singleton
  @Provides
  fun provideSchedulerProviderInterface(): SchedulerProviderInterface = SchedulerProvider

  @Singleton
  @Provides
  fun provideApiRepositoryInterface(api: Api): ApiRepositoryInterface {
    return ApiRepository(api)
  }
}
