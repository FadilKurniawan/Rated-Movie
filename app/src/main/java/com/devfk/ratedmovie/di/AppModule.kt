package com.devfk.ratedmovie.di

import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.api.BaseService
import com.devfk.ratedmovie.data.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constant.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL:String):APIService =
        BaseService.getClient()
}