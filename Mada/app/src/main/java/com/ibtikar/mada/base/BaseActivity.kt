package com.ibtikar.mada.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject



abstract class BaseActivity<repository : BaseRepository,
        MBaseViewModel : BaseViewModel<repository>>
    : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private lateinit var viewModel: MBaseViewModel

    private lateinit var viewModelFactory: ViewModelFactory

    lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getBaseViewModel()
        viewModelFactory = getBaseViewModelFactory()

        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)

        viewModel.loading.observe(this, Observer {
            if (it) showLoading(getLoadingView())
            else hideLoading(getLoadingView())
        })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                hideLoading(getLoadingView())
                showError(it)
            }
        })
        setContentView(getLayoutResourceId())
        setUpView()

    }

    fun showError(it: Any?) {
        if (it != null) {
        }
    }


    abstract fun getBaseViewModel(): MBaseViewModel

    abstract fun getBaseViewModelFactory(): ViewModelFactory

    abstract fun getLayoutResourceId(): Int

    abstract fun getLoadingView(): Boolean

    abstract fun setUpView()

    protected fun hideLoading(loadingStatus: Boolean) {
        if (loadingStatus)
            progressDialog.dismiss()

    }

    protected fun showLoading(loadingStatus: Boolean) {
        if (loadingStatus)
            progressDialog.show()

    }

    override fun onDestroy() {
        progressDialog.dismiss()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

}
