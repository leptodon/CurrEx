package ru.cactus.currex.di

import org.koin.dsl.module
import ru.cactus.currex.data.repository.CurrencyRepository

val repositoryModule = module {
    single { CurrencyRepository(get()) }
}