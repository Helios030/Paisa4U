package com.paisa.four_u.net

import com.paisa.four_u.BuildConfig
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.util.SpRepository
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object paisa_HttpManager {

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    private val netInterceptor: paisa_NetInterceptor = paisa_NetInterceptor()

    private val retrofit: Retrofit by lazy {
        return@lazy Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    private fun httpClient(): OkHttpClient {
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }


        val cacheFile = File(paisa_RApplication.instance.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小

        return OkHttpClient.Builder()
            .retryOnConnectionFailure(false)/*不重复请求*/
            .addInterceptor(addQueryParameterInterceptor())//参数添加
            .addInterceptor(addHeaderInterceptor())//token过滤
            .addNetworkInterceptor(netInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(cache)//添加缓存
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    }


    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("token", SpRepository.token)
                .header("language", SpRepository.languageCode)
                .header("version",BuildConfig.VERSION_NAME)
                .header("device-type", "ANDROID")
                .header("app-name", BuildConfig.APP_NAME)
                .header("app-platform", BuildConfig.CHANNEL)
                .header("api-version", "1.2")
                .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }


    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor {
                chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
//          Provide your custom parameter here
//          .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
//          .addQueryParameter("deviceModel", AppUtil.getMobileModel())
            .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }


    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}