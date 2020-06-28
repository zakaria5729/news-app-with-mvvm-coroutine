package com.zakariahossain.newsmvvm.util

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

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
