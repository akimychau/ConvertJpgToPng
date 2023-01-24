package ru.akimychev.convertjpgtopng

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

class ConvertingApp : Application() {
    companion object {
        lateinit var instance: ConvertingApp
    }

    private val cicerone: Cicerone<Router> by lazy { Cicerone.create() }

    val navigationHolder = cicerone.getNavigatorHolder()
    val router = cicerone.router

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}