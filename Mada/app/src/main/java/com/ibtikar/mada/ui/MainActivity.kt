package com.ibtikar.mada.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaFormat
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ibtikar.mada.base.BaseActivity
import com.ibtikar.mada.base.PayFortData
import com.ibtikar.mada.base.ViewModelFactory
import com.payfort.fort.android.sdk.base.FortSdk
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager
import com.payfort.sdk.android.dependancies.base.FortInterfaces
import com.payfort.sdk.android.dependancies.commons.Constants
import com.payfort.sdk.android.dependancies.models.FortRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject


class MainActivity : BaseActivity<MainRepository, MainViewModel>() {


    private lateinit var data: PayFortData

    //WS params
    private val KEY_MERCHANT_IDENTIFIER = "merchant_identifier"
    private val KEY_SERVICE_COMMAND = "service_command"
    private val KEY_DEVICE_ID = "device_id"
    private val KEY_LANGUAGE = "language"
    private val KEY_ACCESS_CODE = "access_code"
    private val KEY_SIGNATURE = "signature"

    private val TEST_TOKEN_URL = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi"

    //Statics
    private val MERCHANT_IDENTIFIER = "Use your registered merchant identifier"//"Cyc0HZ9xV6j";//;
    private val ACCESS_CODE = "Use your registered access code which is generated at the time of regitration at payfort"//"zx0IPmPy5jp1vAz8Kpg7";//;
    private val SHA_TYPE = "SHA-256"
    private val SHA_REQUEST_PHRASE = "asfasdsadee"
    val LANGUAGE_TYPE = "en"

    private lateinit var fortRequest: FortRequest
    private lateinit var fortCallback: FortCallBackManager

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun getBaseViewModel(): MainViewModel = viewModel

    override fun getBaseViewModelFactory(): ViewModelFactory = viewModelFactory

    override fun getLayoutResourceId(): Int {
        return com.ibtikar.mada.R.layout.activity_main
    }

    override fun getLoadingView(): Boolean {
        return true
    }

    override fun setUpView() {

        fortCallback = FortCallBackManager.Factory.create()

        viewModel.getToken(getTokenParams())
        viewModel.tokenResponse.observe(this, Observer {
            if (it != null) {
                data = it
            }
        })

        pay.setOnClickListener {
            if (data != null && data.sdkToken !=null) {
                fortRequest = FortRequest()
                fortRequest.requestMap = collectRequestMap(data)
                fortRequest.isShowResponsePage = true

                callSdk(fortRequest)
            }
        }
    }

    private fun collectRequestMap(it: PayFortData): MutableMap<String, Any>? {
        val requestMap = HashMap<String, Any>()
        requestMap["command"] = "PURCHASE"
        requestMap["merchant_reference"] = (System.currentTimeMillis()).toString()
        requestMap["amount"] = (pay.text.toString().trim().toDouble() * 100).toInt().toString()
        requestMap["currency"] = "SAR"
        requestMap["language"] = it.language!!
        requestMap["customer_email"] = "test@gmail.com"
        requestMap["sdk_token"] = it.sdkToken!!
        requestMap["payment_option"] = "MADA"

        return requestMap
    }


    @SuppressLint("BinaryOperationInTimber")
    private fun callSdk(fortrequest: FortRequest) {
        FortSdk.getInstance().registerCallback(this, fortrequest,
                FortSdk.ENVIRONMENT.TEST, 555, fortCallback, true,
                object : FortInterfaces.OnTnxProcessed {

                    override fun onSuccess(p0: MutableMap<String, Any>?, p1: MutableMap<String, Any>?) {
                        Toast.makeText(this@MainActivity, "onSuccess  " +
                                p0.toString() + " \n" + p1.toString(), Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(p0: MutableMap<String, Any>?, p1: MutableMap<String, Any>?) {
                        Toast.makeText(this@MainActivity, "onFailure  " +
                                p0.toString() + " \n" + p1.toString(), Toast.LENGTH_LONG).show()

                    }

                    override fun onCancel(p0: MutableMap<String, Any>?, p1: MutableMap<String, Any>?) {
                        Toast.makeText(this@MainActivity, "onCancel  " +
                                p0.toString() + " \n" + p1.toString(), Toast.LENGTH_LONG).show()
                    }

                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fortCallback.onActivityResult(requestCode, resultCode, data)
    }


    fun getTokenParams(): String {
        val jsonObject = JSONObject()
        val device_id = FortSdk.getDeviceId(this)
        val concatenatedString = SHA_REQUEST_PHRASE +
                KEY_ACCESS_CODE + "=" + ACCESS_CODE +
                KEY_DEVICE_ID + "=" + device_id +
                MediaFormat.KEY_LANGUAGE + "=" + LANGUAGE_TYPE +
                KEY_MERCHANT_IDENTIFIER + "=" + MERCHANT_IDENTIFIER +
                KEY_SERVICE_COMMAND + "=" + Constants.FORT_PARAMS.SDK_TOKEN +
                SHA_REQUEST_PHRASE

        jsonObject.put(KEY_SERVICE_COMMAND, Constants.FORT_PARAMS.SDK_TOKEN)
        jsonObject.put(KEY_MERCHANT_IDENTIFIER, MERCHANT_IDENTIFIER)
        jsonObject.put(KEY_ACCESS_CODE, ACCESS_CODE)
        val signature = getSignatureSHA256(concatenatedString)
        jsonObject.put(KEY_SIGNATURE, signature)
        jsonObject.put(KEY_DEVICE_ID, device_id)
        jsonObject.put(MediaFormat.KEY_LANGUAGE, LANGUAGE_TYPE)

        Log.e("concatenatedString", concatenatedString)
        Log.e("signature", signature)
        Log.e("JsonString", jsonObject.toString())
        return jsonObject.toString()
    }


    private fun getSignatureSHA256(s: String): String {
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance(SHA_TYPE)
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            return String.format("%0" + messageDigest.size * 2 + 'x'.toString(), BigInteger(1, messageDigest))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

}
