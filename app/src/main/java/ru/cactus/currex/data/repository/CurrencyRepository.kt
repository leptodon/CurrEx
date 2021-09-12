package ru.cactus.currex.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.cactus.currex.data.api.CurrencyApiService
import ru.cactus.currex.data.response_models.ValCurs

class CurrencyRepository(private val currencyApiService: CurrencyApiService) {

    fun getCurrency(): Flow<ValCurs?> = flow {
        val response = currencyApiService.getCurrencyList()
        emit(response)
    }.flowOn(Dispatchers.IO)
}