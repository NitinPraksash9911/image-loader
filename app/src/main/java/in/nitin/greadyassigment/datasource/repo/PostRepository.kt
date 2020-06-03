package `in`.nitin.greadyassigment.datasource.repo

import `in`.nitin.greadyassigment.datasource.network.ApiFactory
import `in`.nitin.greadyassigment.datasource.network.ApiService
import `in`.nitin.greadyassigment.datasource.network.ResponseHandler
import javax.inject.Inject

class PostRepository @Inject constructor(private var apiFactory: ApiFactory) : ResponseHandler() {

    suspend fun fetchPostData() = getResult {
        apiFactory.createService(ApiService::class.java)
            .getRedditPostData()
    }

}