package com.kaerusworld.newsapp.di

import android.content.Context
import android.net.ConnectivityManager
import com.kaerusworld.newsapp.data.db.NewsDao
import com.kaerusworld.newsapp.data.network.NewsApiService
import com.kaerusworld.newsapp.data.repository.NewsRepositoryImpl
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import com.kaerusworld.newsapp.domain.usecase.FetchArticleUseCase
import com.kaerusworld.newsapp.domain.usecase.GetOfflineNewsUseCase
import com.kaerusworld.newsapp.common.Constants.BASE_URL
import com.kaerusworld.newsapp.common.Constants.BASE_URL_FOR_LIKE_COMMENT
import com.kaerusworld.newsapp.common.NetworkUtils
import com.kaerusworld.newsapp.data.network.ArticleInfoApi
import com.kaerusworld.newsapp.domain.usecase.GetNewsCommentsUseCase
import com.kaerusworld.newsapp.domain.usecase.GetNewsLikesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InfoRetrofit


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
    fun provideNetworkUtils(connectivityManager: ConnectivityManager): NetworkUtils {
        return NetworkUtils(connectivityManager)
    }

    @Provides
    @Singleton
    @NewsRetrofit
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    @InfoRetrofit
    fun provideInfoRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        return Retrofit.Builder().baseUrl(BASE_URL_FOR_LIKE_COMMENT).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(@NewsRetrofit retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        apiService: NewsApiService,
        newsDao: NewsDao,
        networkUtils: NetworkUtils,
        articleInfoApi: ArticleInfoApi
    ): NewsRepository {
        return NewsRepositoryImpl(apiService, newsDao, networkUtils, articleInfoApi)
    }

    @Provides
    fun provideFetchNewsUseCase(newsRepository: NewsRepository): FetchArticleUseCase {
        return FetchArticleUseCase(newsRepository)
    }

    @Provides
    fun provideGetOfflineNewsUseCase(newsRepository: NewsRepository): GetOfflineNewsUseCase {
        return GetOfflineNewsUseCase(newsRepository)
    }


    @Provides
    fun provideGetNewsLikesUseCase(repository: NewsRepository): GetNewsLikesUseCase {
        return GetNewsLikesUseCase(repository)
    }

    @Provides
    fun provideGetNewsCommentsUseCase(repository: NewsRepository): GetNewsCommentsUseCase {
        return GetNewsCommentsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideNewsInfoApi(@InfoRetrofit retrofit: Retrofit): ArticleInfoApi {
        return retrofit.create(ArticleInfoApi::class.java)
    }
}