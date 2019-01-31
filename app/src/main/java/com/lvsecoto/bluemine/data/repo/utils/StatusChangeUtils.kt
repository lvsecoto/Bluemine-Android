package com.lvsecoto.bluemine.data.repo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.lvsecoto.bluemine.AppExecutors

fun <T> LiveData<T>.toSuccess(appExecutors: AppExecutors) : LiveData<Resource<T>> =
    MediatorLiveData<Resource<T>>().apply {
        appExecutors.mainThread().execute {
            value = Resource.loading(null)
            addSource(this@toSuccess) {
                value = Resource.success(it)
            }
        }
    }
