package com.ibtikar.mada.remote

import com.ibtikar.mada.base.PayFortData
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by Mariam Eskander on 2019-05-14.
 */

interface AccountServices {

    @POST("paymentApi")
    fun getToken(@Header("content-type")  data: String , @Body string: String): Single<PayFortData>
}