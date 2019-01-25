package com.lvsecoto.bluemine.data.cache.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

inline fun <reified T> SharedPreferences.objectListLiveData(key: String): LiveData<List<T>> =
    SharedPreferenceDao.objectListLiveData(this, key, T::class.java)

inline fun <reified T> SharedPreferences.putObjectOrObjectList(key: String, data: T) =
    SharedPreferenceDao.putObjectOrObjectList(this, key, data)
