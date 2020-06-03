package `in`.nitin.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import android.util.Log
import com.jakewharton.disklrucache.DiskLruCache
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest


/*
*
* DiskCache using LruCache
* */
internal class DiskCache(context: Context) {
    private val TAG = "DISK_CACHE"
    private val DISK_CACHE_SIZE = 1024 * 1024 * 10L // 10MB
    private val DISK_CACHE_SUBDIR = "redditImage"
    private val diskLruCache: DiskLruCache =
        DiskLruCache.open(getDiskCacheDir(context, DISK_CACHE_SUBDIR), 1, 1, DISK_CACHE_SIZE)


    /**
     * @param url : use to get entry from cache
     * */
    fun get(url: String): Bitmap? {
        val key = url.md5()
        val snapshot: DiskLruCache.Snapshot? = diskLruCache.get(key)
        return if (snapshot != null) {
            val inputStream: InputStream = snapshot.getInputStream(0)
            val buffIn = BufferedInputStream(inputStream, 8 * 1024)
            BitmapFactory.decodeStream(buffIn)
        } else {
            null
        }
    }

    /**
     * @param url: this url using as key to storing entry in cache
     * @param bitmap: value for the cache entry
     * */
    fun put(url: String, bitmap: Bitmap) {
        val key: String = url.md5()
        var editor: DiskLruCache.Editor? = null
        try {
            editor = diskLruCache.edit(key)
            if (editor == null) {
                return
            }
            if (writeBitmapToFile(bitmap, editor)) {
                diskLruCache.flush()
                editor.commit()
            } else {
                editor.abort()
            }
        } catch (e: IOException) {
            try {
                editor?.abort()
            } catch (e: IOException) {
                Log.e(TAG, e.message)
            }
            Log.e(TAG, e.message)

        }
    }


    /**
     * @param bitmap
     * @param editor
     * */
    private fun writeBitmapToFile(bitmap: Bitmap, editor: DiskLruCache.Editor): Boolean {
        var out: OutputStream? = null
        try {
            out = BufferedOutputStream(editor.newOutputStream(0), 8 * 1024)
            return bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } finally {
            out?.close()

        }
    }

    /**
     * @param context
     * @param dirName : sub-directory name for cache
     *
     * Check if media is mounted or storage is built-in, if so, try and use external cache dir
     * otherwise use internal cache dir
     * */
    private fun getDiskCacheDir(context: Context, dirName: String): File {

        val cachePath =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                || !isExternalStorageRemovable()
            ) {
                context.externalCacheDir!!.path
            } else {
                context.cacheDir.path
            }

        return File(cachePath + File.separator + dirName)
    }

    /**
     * convert string url key to [MD5] format
     * */
    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}