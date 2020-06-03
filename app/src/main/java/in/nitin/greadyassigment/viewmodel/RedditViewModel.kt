package `in`.nitin.greadyassigment.viewmodel

import `in`.nitin.greadyassigment.datasource.helper.Result
import `in`.nitin.greadyassigment.datasource.model.Child
import `in`.nitin.greadyassigment.datasource.repo.PostRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import javax.inject.Inject

class RedditViewModel @Inject constructor(
    private var stateHandle: SavedStateHandle,
    private var postRepository: PostRepository
) :
    ViewModel() {

    val SAVED_POST_DATA_KEY = "reddit_post_data"

    fun getPostData() = liveData {
        val response = postRepository.fetchPostData()

        when (response.status) {
            Result.Status.LOADING -> {
                emit(Result.loading())
            }
            Result.Status.SUCCESS -> {
                emit(Result.success(response.data))
            }
            Result.Status.ERROR -> {
                emit(Result.error(response.message!!))

            }
        }

    }

    fun savePostInStateHandle(childList: List<Child>) {
        stateHandle.set(SAVED_POST_DATA_KEY, childList)

    }

    fun getSavedPostData(): LiveData<List<Child>> {
        return stateHandle.getLiveData(SAVED_POST_DATA_KEY)
    }


}