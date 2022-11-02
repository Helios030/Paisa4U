package com.paisa.four_u.util.expand

import java.util.regex.Pattern

/**
 * @Author Ben
 * @Date 2022/4/22 11:18
 * @desc:
 */


/**
 * 正则表达式：验证用户名
 */
const val REGEX_USERNAME = "^[a-zA-Z0-9.,\\s]{0,64}$"

/**
 * 正则表达式：验证密码
 */
const val REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$"

/**
 * 正则表达式：验证手机号
 */
const val REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"

/**
 * 正则表达式：验证邮箱
 */
const val REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"

/**
 * 正则表达式：验证汉字
 */
const val REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$"

/**
 * 正则表达式：验证身份证
 */
const val REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)"

/**
 * 正则表达式：验证URL
 */
const val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"

/**
 * 正则表达式：验证IP地址
 */
const val REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)"

/**
 * 正则表达式：验证数字
 */
const val REGEX_NOMBER = "^[0-9]*$"

/**
 * 正则表达式：验证PanCard
 */
const val REGEX_PANCARD = "^[A-Z]{3}[P|F|C|H|A|T|B|L|J|G][A-Z]\\d{4}[A-Z]\$"/*"^([A-Z]{5}[0-9]{4}[A-Z])$"*/


/**
 * 校验用户名
 *
 * @param username
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isUsername(): Boolean {
    return Pattern.matches(REGEX_USERNAME, this)
}

/**
 * 校验密码
 *
 * @param password
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isPassword(): Boolean {
    return Pattern.matches(REGEX_PASSWORD, this)
}

/**
 * 校验手机号
 *
 * @param mobile
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isMobile(): Boolean {
    return Pattern.matches(REGEX_MOBILE, this)
}

/**
 * 校验邮箱
 *
 * @param email
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isEmail(): Boolean {
    return Pattern.matches(REGEX_EMAIL, this)
}

fun CharSequence.isNotEmail(): Boolean {
    return !Pattern.matches(REGEX_EMAIL, this)
}
/**
 * 校验汉字
 *
 * @param chinese
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isChinese(): Boolean {
    return Pattern.matches(REGEX_CHINESE, this)
}

/**
 * 校验身份证
 *
 * @param idCard
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isIDCard(): Boolean {
    return Pattern.matches(REGEX_ID_CARD, this)
}

/**
 * 校验URL
 *
 * @param url
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isUrl(): Boolean {
    return Pattern.matches(REGEX_URL, this)
}

/**
 * 校验IP地址
 *
 * @param ip
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isIPAddr(): Boolean {
    return Pattern.matches(REGEX_IP_ADDR, this)
}

/**
 * 校验数字
 * @param str
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isNumberal(): Boolean {
    return Pattern.matches(REGEX_NOMBER, this)
}


/**
 * 校验PanCard id
 *
 * @param panCard
 * @return 校验通过返回true，否则返回false
 */
fun  CharSequence.isPanCard(): Boolean {
    return Pattern.matches(REGEX_PANCARD,this)
}


