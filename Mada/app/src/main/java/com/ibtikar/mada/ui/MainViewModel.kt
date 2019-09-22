package com.ibtikar.mada.ui

import androidx.lifecycle.MutableLiveData
import com.ibtikar.mada.base.BaseViewModel
import com.ibtikar.mada.base.PayFortData
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * Created by Mariam Eskander on 2019-09-22.
 */


class MainViewModel @Inject constructor() : BaseViewModel<MainRepository>() {
    var tokenResponse: MutableLiveData<PayFortData> = MutableLiveData()


    fun getToken(tokenParams: String) {
        subscribe(
                repository.getToken(tokenParams),
                Consumer {
                    tokenResponse.postValue(it)
                }
        )

    }


}