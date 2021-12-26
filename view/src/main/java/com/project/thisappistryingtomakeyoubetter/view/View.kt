package com.project.thisappistryingtomakeyoubetter.view

import android.view.View

fun View.toggle(isNotEmpty: Boolean){
    visibility = if(isNotEmpty){
        View.GONE
    } else {
        View.VISIBLE
    }
}