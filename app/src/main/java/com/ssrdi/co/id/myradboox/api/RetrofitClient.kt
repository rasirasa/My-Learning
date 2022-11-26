package com.ssrdi.co.id.myradboox.api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {

    /**
     * buat retrofit jadi singleton, biar tidak duplicate object
     */
    companion object {
        private var instance: RadbooxApi? = null

        private const val BASE_URL = "https://api.radboox.com/"

        @Synchronized
        fun getInstance(context: Context): RadbooxApi {

            if (instance == null) {
                val gson = GsonBuilder().setLenient().create()
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(createOkHttpClient(context))
                    .build()
                    .create(RadbooxApi::class.java)
            }
            return instance as RadbooxApi
        }

        private fun createOkHttpClient(context: Context): OkHttpClient {
            // Create the Collector
            val chuckerCollector = ChuckerCollector(
                context = context,
                // Toggles visibility of the notification
                showNotification = true,
                // Allows to customize the retention period of collected data
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )

            // Create the Interceptor
            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                // The previously created Collector
                .collector(chuckerCollector)
                // The max body content length in bytes, after this responses will be truncated.
                .maxContentLength(250_000L)
                // List of headers to replace with ** in the Chucker UI
                .redactHeaders("Auth-Token", "Bearer")
                // Read the whole response body even when the client does not consume the response completely.
                // This is useful in case of parsing errors or when the response body
                // is closed before being read like in Retrofit with Void and Unit types.
                .alwaysReadResponseBody(true)
                // Use decoder when processing request and response bodies. When multiple decoders are installed they
                // are applied in an order they were added.
                .build()

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                .addInterceptor(chuckerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        }
    }
}