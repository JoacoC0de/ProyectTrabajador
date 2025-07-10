package com.example.proyecttrabajador.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TokenProvider {
    var token: String? = null
}

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor { TokenProvider.token })
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://trabajos.jmacboy.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}