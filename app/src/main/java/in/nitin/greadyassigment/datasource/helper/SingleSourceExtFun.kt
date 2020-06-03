package `in`.nitin.greadyassigment.datasource.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

/**
 * [Result.Status.SUCCESS] - with data from network
 * [Result.Status.ERROR] - if error has occurred
 * [Result.Status.LOADING]
 */
fun <T> resultLiveData(
    networkCall: () -> LiveData<T>
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        val source = networkCall.invoke().map { Result.success(it) }
        emitSource(source)
    }