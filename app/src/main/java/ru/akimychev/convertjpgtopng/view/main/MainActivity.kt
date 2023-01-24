package ru.akimychev.convertjpgtopng.view.main

import android.os.Bundle
import com.github.terrakok.cicerone.androidx.AppNavigator
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.akimychev.convertjpgtopng.ConvertingApp
import ru.akimychev.convertjpgtopng.R
import ru.akimychev.convertjpgtopng.core.OnBackPressedListener
import ru.akimychev.convertjpgtopng.databinding.ActivityMainBinding
import ru.akimychev.convertjpgtopng.presenter.MainActivityPresenter


class MainActivity : MvpAppCompatActivity(), MainView {

    private val navigator = AppNavigator(this, R.id.containerMain)
    private lateinit var binding: ActivityMainBinding

    private val presenter by moxyPresenter { MainActivityPresenter(ConvertingApp.instance.router) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        ConvertingApp.instance.navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        ConvertingApp.instance.navigationHolder.removeNavigator()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach { currentFragment ->
            if (currentFragment is OnBackPressedListener && currentFragment.onBackPressed()) {
                return
            }
        }
        presenter.onBackPressed()
    }
}