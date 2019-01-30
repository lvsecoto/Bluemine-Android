package com.lvsecoto.bluemine.data.repo

import android.content.SharedPreferences
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.utils.getObject
import com.lvsecoto.bluemine.data.cache.utils.putObjectOrObjectList
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.vo.RetrofitSettings
import com.lvsecoto.bluemine.utils.IOLiveData

private const val KEY_RETROFIT_SETTING = "KEY_RETROFIT_SETTING"

/**
 * 单例
 */
class SettingsRepository(
    private val appExecutors: AppExecutors,
    private val pref: SharedPreferences
) {
    val retrofitSetting =
        object : IOLiveData<Resource<RetrofitSettings>>(appExecutors) {
            override fun onExecute() {
                setValue(Resource.success(pref.getObject(KEY_RETROFIT_SETTING)))
            }
        }

    fun setRetrofitSetting(retrofitSettings: RetrofitSettings) =
        object : IOLiveData<Resource<Void>>(appExecutors) {
            override fun onExecute() {
                pref.putObjectOrObjectList(KEY_RETROFIT_SETTING, retrofitSettings)
                setValue(Resource.success(null))
            }
        }
}
