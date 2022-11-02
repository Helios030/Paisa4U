package com.paisa.four_u.ui.userData.first

import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.PhotoModel
import com.paisa.four_u.model.RegionModel
import com.paisa.four_u.model.UserDataModel
import com.paisa.four_u.model.UserDataOneModel
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.expand.createBody
import com.paisa.four_u.util.expand.toMap
import java.io.File

/**
 * @Author Ben
 * @Date 2022/4/15 16:20
 * @desc:
 */
class paisa_PersonalVMPaisa : paisa_BaseVM() {

    var liveUserDataBean = MutableLiveData<UserDataModel>()
    var liveCityBean = MutableLiveData<List<RegionModel>>()
    var liveUploadState = MutableLiveData<Boolean>()
    var livePhotoBean = MutableLiveData<PhotoModel>()
    var livePhotoBackBean = MutableLiveData<PhotoModel>()


    fun getUserDataOne() {
        request({
            mApiService.getUserDataOne(HashMap<String, Any>().apply { this["part_info"] = "1" }.createBody())
        }, {
            liveUserDataBean.value = this
        })
    }

    fun uploadUserDataOne(userDataOneModel: UserDataOneModel) {
        request({
            mApiService.upUserDataOne(userDataOneModel.toMap().createBody())
        }, {
            liveUploadState.value = true
        },{
            liveUploadState.value = false
        })
    }


    fun uploadImg(file: File){
        val type = MimeTypeMap.getFileExtensionFromUrl(file.path)
        val imgData: String =  Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
        val pictureMap = HashMap<String, Any>()
        pictureMap["content"] = imgData
        pictureMap["extension"] = type
        val dataMap = HashMap<String, Any>()
        dataMap["picture_type"] = "1"
        dataMap["picture"] = pictureMap
        request({
            mApiService.upPicture(dataMap.createBody())

        }, {
            livePhotoBean.value=this
            Slog.d("===>  图片上传返回  $this  ")
        })
    }

//    r2022年10月18日 身份证背面照片修改为类型2
    fun uploadImgBack(file: File){
        val type = MimeTypeMap.getFileExtensionFromUrl(file.path)
        val imgData: String =  Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
        val pictureMap = HashMap<String, Any>()
        pictureMap["content"] = imgData
        pictureMap["extension"] = type
        val dataMap = HashMap<String, Any>()
        dataMap["picture_type"] = "2"
        dataMap["picture"] = pictureMap
        request({
            mApiService.upPicture(dataMap.createBody())

        }, {
            livePhotoBackBean.value=this
            Slog.d("===>  图片上传返回  $this  ")
        })
    }




    fun getCityList(cityId: String) {
        request({
            val map = HashMap<String, Any>()
            map["id"] = cityId
            mApiService.getRegionList(map.createBody())
        }, {
            liveCityBean.value=this
        })
    }



}