package com.ibtikar.mada.ui

import com.google.gson.Gson
import com.ibtikar.mada.base.BaseRepository
import com.ibtikar.mada.base.PayFortData
import com.ibtikar.mada.remote.AccountServices
import io.reactivex.Single
import javax.inject.Inject


/**
 * Created by Mariam Eskander on 2019-09-22.
 */


class MainRepository @Inject
constructor() : BaseRepository() {

    @Inject
    lateinit var api: AccountServices

    fun getToken(tokenParams: String): Single<PayFortData> {
        return api.getToken("application/json",Gson().toJson(tokenParams))
    }


}
