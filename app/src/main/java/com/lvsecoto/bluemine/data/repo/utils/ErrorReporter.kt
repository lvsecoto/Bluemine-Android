package com.lvsecoto.bluemine.data.repo.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.data.repo.utils.statusobserver.onError
import com.lvsecoto.bluemine.utils.toast.showToast

fun <T> LiveData<Resource<T>>.errorReport(fragment: Fragment) {
    this.onError(fragment.viewLifecycleOwner) {
        when (it.message) {
            is String -> fragment.showToast(it.message)
            else -> fragment.showToast(it.message.toString())
        }
    }
}