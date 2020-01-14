package jp.gr.java_conf.foobar.remainder

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

fun getRemindMemoModules(dao: RemindMemoDao) = module {
    single { NotificationService(get()) }
    single { RemindMemoRepository(dao) }
    viewModel { MainViewModel(get(), get()) }
}