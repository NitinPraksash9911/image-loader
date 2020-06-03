package `in`.nitin.greadyassigment.datasource.network

import `in`.nitin.greadyassigment.datasource.model.RedditModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("r/images/hot.json")
    suspend fun getRedditPostData(): Response<RedditModel>
}