package `in`.nitin.greadyassigment.di

import `in`.nitin.greadyassigment.di.qualifier.ViewModelKey
import `in`.nitin.greadyassigment.viewmodel.RedditViewModel
import `in`.nitin.greadyassigment.viewmodel.ViewModelProviderFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RedditViewModel::class)
    abstract fun bindRedditViewModel(productViewModel: RedditViewModel): ViewModel

}