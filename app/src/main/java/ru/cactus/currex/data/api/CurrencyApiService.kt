package ru.cactus.currex.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.cactus.currex.data.response_models.ValCurs

interface CurrencyApiService {
    @GET("/scripts/XML_daily.asp")
    suspend fun getCurrencyList(): ValCurs
}