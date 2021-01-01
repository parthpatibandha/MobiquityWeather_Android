package org.codejudge.application.di

import org.codejudge.application.data.contract.HomeRepo
import org.codejudge.application.data.repository.HomeRepository
import org.koin.dsl.module.module

/**
 * Created Koin module for Repository class
 */

val repositoryModule = module {
    single<HomeRepo> { HomeRepository(get()) }
}