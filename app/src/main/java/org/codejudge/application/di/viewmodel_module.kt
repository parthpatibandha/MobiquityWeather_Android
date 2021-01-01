package org.codejudge.application.di

import org.codejudge.application.presentation.home.HomeViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created Koin module for ViewModel class
 */
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}