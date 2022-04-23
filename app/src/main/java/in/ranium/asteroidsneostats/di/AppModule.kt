package `in`.ranium.asteroidsneostats.di

import android.content.Context
import android.content.SharedPreferences
import `in`.ranium.asteroidsneostats.BuildConfig
import `in`.ranium.asteroidsneostats.core.ApiInterface
import `in`.ranium.asteroidsneostats.core.Constants
import `in`.ranium.asteroidsneostats.ui.BaseActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .connectTimeout(Constants.API_REQUEST_TIMEOUT_IN_MINUTES,
                TimeUnit.MINUTES)
            .writeTimeout(Constants.API_REQUEST_TIMEOUT_IN_MINUTES,
                TimeUnit.MINUTES)
            .readTimeout(Constants.API_REQUEST_TIMEOUT_IN_MINUTES,
                TimeUnit.MINUTES)
//            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .connectTimeout(Constants.API_REQUEST_TIMEOUT_IN_MINUTES,
                TimeUnit.MINUTES)
            .writeTimeout(Constants.API_REQUEST_TIMEOUT_IN_MINUTES,
                TimeUnit.MINUTES)
            .readTimeout(Constants.API_REQUEST_TIMEOUT_IN_MINUTES,
                TimeUnit.MINUTES)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext ctx: Context
    ) = ctx

    @Provides
    @Singleton
    fun provideSharedPreferences(
        ctx: Context
    ): SharedPreferences =
        ctx.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideBaseActivity(): BaseActivity = BaseActivity()
}