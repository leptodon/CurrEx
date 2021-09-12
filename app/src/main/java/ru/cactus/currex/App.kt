package ru.cactus.currex

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.cactus.currex.di.networkModule
import ru.cactus.currex.di.repositoryModule
import ru.cactus.currex.di.viewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}