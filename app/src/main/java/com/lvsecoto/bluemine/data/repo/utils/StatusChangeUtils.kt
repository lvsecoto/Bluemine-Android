package com.lvsecoto.bluemine.data.repo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.toSuccess() : LiveData<Resource<T>> =
    MediatorLiveData<Resource<T>>().apply {
        value = Resource.loading(null)
        addSource(this@toSuccess) {
            value = Resource.success(it)
        }
    }
