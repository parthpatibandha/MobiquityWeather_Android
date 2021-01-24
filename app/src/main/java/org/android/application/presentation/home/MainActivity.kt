package org.android.application.presentation.home

import android.os.Bundle
import org.android.application.R
import org.android.application.databinding.ActivityMainBinding
import org.android.application.presentation.core.BaseActivity
import org.android.application.presentation.home.fragments.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        addCityListFragment()
    }

    private fun addCityListFragment() {
        replaceFragmentWithPop(
            R.id.frameContainer,
            CityListFragment(),
            CityListFragment::class.java.simpleName
        )
    }

    fun addCityWiseWeatherFragment() {
        replaceFragment(
            R.id.frameContainer,
            CityWeatherFragment(),
            CityWeatherFragment::class.java.simpleName,
            true
        )
    }

    fun addSearchCityByMapFragment() {
        replaceFragment(
            R.id.frameContainer,
            SearchCityByMapFragment(),
            SearchCityByMapFragment::class.java.simpleName,
            true
        )
    }

    fun addHelpFragment(){
        replaceFragment(
            R.id.frameContainer,
            HelpFragment(),
            HelpFragment::class.java.simpleName,
            true
        )
    }

    fun addSettingFragment(){
        replaceFragment(
            R.id.frameContainer,
            SettingsFragment(),
            SettingsFragment::class.java.simpleName,
            true
        )
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
