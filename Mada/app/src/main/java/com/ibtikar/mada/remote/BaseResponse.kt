package com.ibtikar.mada.remote


/**
 * Created by Mariam Eskander on 2019-05-12.
 */


sealed class ERRORS {
    object NO_INTRERNET : ERRORS()
    object UN_EXPECTED : ERRORS()
    object UNKNOWN : ERRORS()
}

data class Error(var code: ERRORS?, var error: String?)
data class Success(var message: String?)
open class BaseResponse(
    open var Message: String? = "",
    open var Code: Int? = null
)