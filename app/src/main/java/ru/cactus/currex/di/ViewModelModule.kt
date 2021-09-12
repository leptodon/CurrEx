package ru.cactus.currex.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.cactus.currex.ui.main.MainViewModel

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}