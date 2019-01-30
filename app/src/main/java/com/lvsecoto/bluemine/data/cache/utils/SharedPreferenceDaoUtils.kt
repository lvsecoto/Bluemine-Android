package com.lvsecoto.bluemine.data.cache.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

inline fun <reified T> SharedPreferences.objectLiveData(key: String): LiveData<T> =
    SharedPreferenceDao.objectLiveData(this, key, T::class.java)

inline fun <reified T> SharedPreferences.getObject(key: String): T? =
    SharedPreferenceDao.getObject(this, key, T::class.java)

inline fun <reified T> SharedPreferences.objectListLiveData(key: String): LiveData<List<T>> =
    SharedPreferenceDao.objectListLiveData(this, key, T::class.java)

inline fun <reified T> SharedPreferences.getObjectList(key: String): List<T>? =
    SharedPreferenceDao.getObjectList(this, key, T::class.java)

inline fun <reified T> SharedPreferences.putObjectOrObjectList(key: String, data: T) =
    SharedPreferenceDao.putObjectOrObjectList(this, key, data)
