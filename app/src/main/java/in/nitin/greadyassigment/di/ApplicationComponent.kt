package `in`.nitin.greadyassigment.di

import `in`.nitin.greadyassigment.application.App
import `in`.nitin.greadyassigment.di.qualifier.ApplicationContext
import `in`.nitin.greadyassigment.ui.BaseFragment
import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class, ApplicationModule::class]
)
interface ApplicationComponent {

    fun inject(myapplication: App?)
//
    fun inject(baseFragment: BaseFragment)

    @ApplicationContext
    fun getApplication(): Application;

}