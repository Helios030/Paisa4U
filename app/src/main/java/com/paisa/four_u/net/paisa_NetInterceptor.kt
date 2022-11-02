package com.paisa.four_u.net

import com.paisa.four_u.util.expand.isNetAvailable
import com.paisa.four_u.util.expand.show
import com.paisa.four_u.util.slog
import okhttp3.Interceptor
import okhttp3.Response

class paisa_NetInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        val code = response.code()
        code.slog("intercept ")
        val available = isNetAvailable()
        if (!available) {
            "There is currently no network, please refresh after connecting to the network".show()
        } else {
            when (code) {
                404 -> "Request does not exist...".show()
                408 -> "Network connection timed out, please try again later".show()
                500, 501, 502, 503, 504, 505 -> "The network has gone away, please wait a moment...".show()
                else -> {}
            }
        }
        return response
    }

}