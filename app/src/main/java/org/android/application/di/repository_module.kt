package org.android.application.di

import org.android.application.data.contract.HomeRepo
import org.android.application.data.repository.HomeRepository
import org.koin.dsl.module.module

/**
 * Created Koin module for Repository class
 */

val repositoryModule = module {
    single<HomeRepo> { HomeRepository(get(), get()) }
}