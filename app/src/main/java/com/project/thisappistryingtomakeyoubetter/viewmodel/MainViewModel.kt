package com.project.thisappistryingtomakeyoubetter.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    val includeYesterday = MutableLiveData<Boolean>()
    fun setSetting(includedYesterday: Boolean){
        this.includeYesterday.value = includedYesterday
    }

    val currentPosition = MutableLiveData<Int>()

    val currentFragment = MutableLiveData<Fragment>()

}