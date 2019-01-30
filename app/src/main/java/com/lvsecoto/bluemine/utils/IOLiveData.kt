package com.lvsecoto.bluemine.utils

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.repo.utils.Status

/**
 * 适合执行异步任务，开始的时候，LiveData状态会被设置为[Status.LOADING]，回调[onExecute]在IO线程中执行，[setValue]在主线程执行
 */
abstract class IOLiveData<T>(
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Resource<T>>()

    init {
        appExecutors.diskIO().execute {
            setValue(Resource.loading(null))
            onExecute()
        }
    }

    fun asLiveData(): LiveData<Resource<T>> =
        result

    @WorkerThread
    abstract fun onExecute()

    protected fun setValue(value: Resource<T>) {
        appExecutors.mainThread().execute {
            result.value = value
        }
    }
}