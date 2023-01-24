package ru.akimychev.convertjpgtopng.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.akimychev.convertjpgtopng.view.converterFragment.ConverterFragment

object ConvertingScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        return ConverterFragment.getInstance()
    }
}