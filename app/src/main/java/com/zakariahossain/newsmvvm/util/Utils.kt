package com.zakariahossain.newsmvvm.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

const val SEARCH_NEWS_TIME_DELAY = 800L
const val QUERY_PAGE_SIZE = 15

fun ProgressBar.isShowProgressBar(visibility: Boolean) {
    if (visibility) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun TextView.isVisibleView(listSize: Int) {
    if (listSize <= 0) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun View.snackBar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, length).show()
}

fun <T>checkError(e: Exception, mutableLiveData: MutableLiveData<Resource<T>>) {
    when (e) {
        is IOException -> mutableLiveData.postValue(
            Resource.Error(e.message ?: "An unknown exception occurred")
        )
        else -> mutableLiveData.postValue(Resource.Error("Conversion Error"))
    }
}

