package com.paisa.four_u.util


typealias sp = PreferencesUtil

object SpRepository {

    internal object Key {
        val KEY_UUID = "UUID"
        val KEY_TOKEN = "KEY_TOKEN"
        val KEY_LANGUAGECODE = "KEY_LANGUAGECODE"
        val KEY_IS_REFERRER = "KEY_IS_REFERRER"
        val IS_SHOW_DIALOG = "IS_SHOW_DIALOG"
        val IS_FIRST = "IS_FIRST"
        val IS_FIRST_OPEN_PP = "IS_FIRST_OPEN_PP"
        val PHONE = "PHONE"
        val ID_PHOTO = "ID_PHOTO"
        val ID_PHOTO_Hand = "ID_PHOTO_Hand"
        val CUSTOMER_SERVICE = "CUSTOMER_SERVICE"
        val OAID = "OAID"
        val LOCATION_LAT = "LOCATION_LAT"
        val LOCATION_LON = "LOCATION_LON"
        val UPLOAD_TIME = "UPLOAD_TIME"





    }

    var uploadTime:Long
        get() = sp.getLong(Key.UPLOAD_TIME)
        set(value) = sp.saveValue(Key.UPLOAD_TIME, value)



    var oaid: String
        get() = sp.getString(Key.OAID)
        set(value) = sp.saveValue(Key.OAID, value)

    var location_lat: String
        get() = sp.getString(Key.LOCATION_LAT)
        set(value) = sp.saveValue(Key.LOCATION_LAT, value)

    var location_lon: String
        get() = sp.getString(Key.LOCATION_LON)
        set(value) = sp.saveValue(Key.LOCATION_LON, value)



    var uuid: String
        get() = sp.getString(Key.KEY_UUID)
        set(value) = sp.saveValue(Key.KEY_UUID, value)

    var token: String
        get() = sp.getString(Key.KEY_TOKEN)
        set(value) = sp.saveValue(Key.KEY_TOKEN, value)

    var languageCode: String
        get() = sp.getString(Key.KEY_LANGUAGECODE, default = "EN")
        set(value) = sp.saveValue(Key.KEY_LANGUAGECODE, value)

    var isReferrer: Boolean
        get() = sp.getBoolean(Key.KEY_IS_REFERRER, default = true)
        set(value) = sp.saveValue(Key.KEY_IS_REFERRER, value)

    var isShowDialog: Boolean
        get() = sp.getBoolean(Key.IS_SHOW_DIALOG, default = true)
        set(value) = sp.saveValue(Key.IS_SHOW_DIALOG, value)


    var isFirst: Boolean
        get() = sp.getBoolean(Key.IS_FIRST, default = true)
        set(value) = sp.saveValue(Key.IS_FIRST, value)

    var isFirstOpenPP: Boolean
        get() = sp.getBoolean(Key.IS_FIRST_OPEN_PP, default = true)
        set(value) = sp.saveValue(Key.IS_FIRST_OPEN_PP, value)

    var phone: String
        get() = sp.getString(Key.PHONE)
        set(value) = sp.saveValue(Key.PHONE, value)

    var idPhoto: String
        get() = sp.getString(Key.ID_PHOTO)
        set(value) = sp.saveValue(Key.ID_PHOTO, value)

    var idPhotoHand: String
        get() = sp.getString(Key.ID_PHOTO_Hand)
        set(value) = sp.saveValue(Key.ID_PHOTO_Hand, value)


    var customerService: String
        get() = sp.getString(Key.CUSTOMER_SERVICE)
        set(value) = sp.saveValue(Key.CUSTOMER_SERVICE, value)


}