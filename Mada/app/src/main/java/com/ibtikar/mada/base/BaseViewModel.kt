package com.ibtikar.mada.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibtikar.mada.remote.httpErrors.RetrofitException
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

abstract class BaseViewModel<Repository : BaseRepository> : ViewModel() {

    @Inject
    lateinit var repository: Repository

    private val compositeDisposable = CompositeDisposable()
    var error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()


    fun handleError(exception: Throwable) {
        if (exception is RetrofitException) {
            when (exception.getKind()) {
                RetrofitException.Kind.NETWORK -> {
                    error.value = "no internet connection"
                }
                RetrofitException.Kind.HTTP -> {
                    error.value =  ""
                }
                RetrofitException.Kind.UNEXPECTED -> {
                    error.value = ""
                }
            }
        } else {
            error.value =  ""
        }
    }


    fun getError(exception: Throwable): String {
        if (exception is RetrofitException) {
            when (exception.getKind()) {
                RetrofitException.Kind.NETWORK -> {
                    return "no internet connection"

                }
                RetrofitException.Kind.HTTP -> {
                    return  ""

                }
                RetrofitException.Kind.UNEXPECTED -> {
                    return ""

                }
            }
        } else {
            return ""

        }
    }

    fun <T> subscribe(
        observable: Observable<T>,
        success: Consumer<T>,
        error: Consumer<Throwable>,
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeOnMainThread: Boolean = true
    ) {

        val observerScheduler =
            if (observeOnMainThread) AndroidSchedulers.mainThread()
            else subscribeScheduler

        compositeDisposable.add(
            observable
                .subscribeOn(subscribeScheduler)
                .observeOn(observerScheduler)
                .subscribe(success, error)
        )
    }

    fun <T> subscribe(
        observable: Single<T>,
        success: Consumer<T>,
        error: Consumer<Throwable> = Consumer {},
        subscribeScheduler: Scheduler = Schedulers.io(),
        observeOnMainThread: Boolean = true,
        showLoading: Boolean = true
    ) {

        val observerScheduler =
            if (observeOnMainThread) AndroidSchedulers.mainThread()
            else subscribeScheduler

        compositeDisposable.add(observable
            .subscribeOn(subscribeScheduler)
            .observeOn(observerScheduler)
            .compose { single ->
                composeSingle<T>(single, showLoading)
            }
            .subscribe(success, error))
    }

    private fun <T> composeSingle(single: Single<T>, showLoading: Boolean = true): Single<T> {
        return single
            .doOnError {
                Log.e("error",it.toString())
                handleError(it)
            }
            .doOnSubscribe {
                loading.postValue(showLoading)
            }
            .doAfterTerminate {
                loading.postValue(false)
            }
    }

    fun clearSubscription() {
        if (compositeDisposable.isDisposed.not()) compositeDisposable.clear()
    }

    override fun onCleared() {
        clearSubscription()
        super.onCleared()
    }
}