package ru.akimychev.convertjpgtopng.presenter

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.akimychev.convertjpgtopng.core.ConvertingScreen
import ru.akimychev.convertjpgtopng.view.main.MainView

class MainActivityPresenter(
    private val router: Router,
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(ConvertingScreen)
    }

    fun onBackPressed() {
        router.exit()
    }
}