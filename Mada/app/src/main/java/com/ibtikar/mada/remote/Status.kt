/**
 * Status of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
 */

package com.ibtikar.mada.remote

enum class Status {
    LOADING,
    LOADED,
    SUCCESS,
    ERROR,
    CONNECTION_ERROR,
    EMPTY_RESPONSE
}
