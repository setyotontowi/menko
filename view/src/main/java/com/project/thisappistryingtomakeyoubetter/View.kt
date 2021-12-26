package com.project.thisappistryingtomakeyoubetter

import android.view.View

fun View.toggle(isNotEmpty: Boolean){
    visibility = if(isNotEmpty){
        View.GONE
    } else {
        View.VISIBLE
    }
}