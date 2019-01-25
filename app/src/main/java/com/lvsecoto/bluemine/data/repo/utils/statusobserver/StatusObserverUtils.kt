package com.lvsecoto.bluemine.data.repo.utils.statusobserver

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.data.repo.utils.Resource

fun <T> LiveData<Resource<T>>.data(owner: LifecycleOwner, observer: (T?) -> Unit) {
    this.observe(owner, StatusObserver.data<T> { observer(it) })
}

fun <T> LiveData<Resource<T>>.onSuccess(owner: LifecycleOwner, observer: (Resource<T>) -> Unit) {
    this.observe(owner, StatusObserver.onSuccess<T> { observer(it) })
}

fun <T> LiveData<Resource<T>>.hasSuccess(owner: LifecycleOwner, observer: (Resource<T>) -> Unit) {
    this.observe(owner, StatusObserver.hasSuccess<T> { observer(it) })
}

fun <T> LiveData<Resource<T>>.onError(owner: LifecycleOwner, observer: (Resource<T>) -> Unit) {
    this.observe(owner, StatusObserver.onError<T> { observer(it) })
}

fun <T> LiveData<Resource<T>>.hasError(owner: LifecycleOwner, observer: (Resource<T>) -> Unit) {
    this.observe(owner, StatusObserver.hasError<T> { observer(it) })
}

