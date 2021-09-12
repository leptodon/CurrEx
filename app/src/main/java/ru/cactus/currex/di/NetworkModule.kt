package ru.cactus.currex.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import ru.cactus.currex.Const
import ru.cactus.currex.data.api.CurrencyApiService

val networkModule = module {
    single { provideRetrofit() }
    single { provideApi<CurrencyApiService>(get()) }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()
}

private fun getBaseUrl(): String {
    return Const.BASE_URL
}

inline fun <reified T> provideApi(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}