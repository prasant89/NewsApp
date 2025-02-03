package com.kaerusworld.newsapp.di

import android.content.Context
import android.net.ConnectivityManager
import com.kaerusworld.newsapp.data.db.NewsDao
import com.kaerusworld.newsapp.data.network.NewsApiService
import com.kaerusworld.newsapp.data.repository.NewsRepositoryImpl
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import com.kaerusworld.newsapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // Create the logging interceptor to log network requests and responses
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Log request/response body
        }

        // Create an OkHttpClient with the logging interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Attach OkHttp client to Retrofit
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: NewsApiService, newsDao: NewsDao, connectivityManager: ConnectivityManager): NewsRepository {
        return NewsRepositoryImpl(apiService, newsDao, connectivityManager)
    }
}