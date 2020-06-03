package `in`.nitin.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ImageView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


class ImageLoader {

    private val TAG = "ImageLoader"
    private var bitmap: Bitmap? = null
    private var context: Context? = null
    private var diskCache: DiskCache? = null
    private var url: String? = null
    private var imageView: ImageView? = null
    private var taskFuture: Future<*>? = null
    private lateinit var handler: Handler

    var processors = Runtime.getRuntime().availableProcessors()
    private val executorService = Executors.newFixedThreadPool(processors + 1)

    /**
     * @param url: Image url
     * */
    fun load(url: String): ImageLoader {
        this.url = url
        return this
    }

    /**
     * @param context
     * */
    fun with(context: Context): ImageLoader {
        diskCache = DiskCache(context)
        this.context = context;
        return this
    }

    /**
     * @param imageView: loading image in this view
     * */
    fun into(imageView: ImageView): ImageLoader {
        this.imageView = imageView
        loadDataOnUI()
        loadData()
        return this
    }

    /**
     * loading image from diskCache if present otherwise from network
     * */
    private fun loadData() {
        try {
            bitmap = diskCache!!.get(url!!)
            if (bitmap != null) {
                imageView!!.setImageBitmap(bitmap)
            } else {
                executorService.submit {
                    val bitmap = getBitmapFromUrl(url!!)
                    val resizedBitmap = getResizedBitmap(bitmap, 500)
                    diskCache!!.put(url!!, resizedBitmap!!)
                    sendDataToUi(resizedBitmap)
                    /**
                     * here we are gracefully shutdown the executorService
                     * */
//                    graceFullyShutDown()
                }

            }

        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }


    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    /**
     * handling data coming from background thread to Ui thread through Looper.
     * In our case we are sending bitmap from background thread to Ui thread.
     * */
    private fun loadDataOnUI() {
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(inputMessage: Message) {
                bitmap = inputMessage.obj as Bitmap
                imageView!!.setImageBitmap(bitmap)
            }
        }
    }

    /**
     * cancel any current running task
     * */
    fun cancelTask() {
        try {
            taskFuture!!.cancel(true)

        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    /**
     * @param bitmap: send bitmap to UI thread using handler
     * */
    private fun sendDataToUi(bitmap: Bitmap) {
        val msg = handler.obtainMessage()
        msg.obj = bitmap
        handler.sendMessage(msg)
    }


    /**
     * use to shutdown the executor service gracefully
     */
    private fun graceFullyShutDown() {
        executorService.shutdown()
        try {
            if (!executorService.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow()
                Log.i(TAG, "imageLoader service shutdown")

            }
        } catch (e: InterruptedException) {
            Log.e(TAG, e.message)
            executorService.shutdownNow()
        }
    }


    /**
     * @param imageUrl: getting bitmap from url using retrofit
     * */
    private fun getBitmapFromUrl(imageUrl: String): Bitmap {
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(imageUrl).build()
        val response: Response = okHttpClient.newCall(request).execute()
        return BitmapFactory.decodeStream(response.body()!!.byteStream())
    }
}