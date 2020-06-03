package `in`.nitin.greadyassigment.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RedditModel {
    @SerializedName("kind")
    @Expose
    var kind: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

}