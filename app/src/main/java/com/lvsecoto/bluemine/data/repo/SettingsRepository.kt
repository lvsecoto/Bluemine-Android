package com.lvsecoto.bluemine.data.repo

import android.content.SharedPreferences
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.utils.getObject
import com.lvsecoto.bluemine.data.cache.utils.putObjectOrObjectList
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.vo.RetrofitSetting
import com.lvsecoto.bluemine.utils.IOLiveData

private const val KEY_RETROFIT_SETTING = "KEY_RETROFIT_SETTING"

/**
 * 单例
 */
class SettingsRepository(
    private val appExecutors: AppExecutors,
    private val pref: SharedPreferences
) {
    fun getRetrofitSetting() =
        object : IOLiveData<RetrofitSetting>(appExecutors) {
            override fun onExecute() {
                setValue(Resource.success(pref.getObject(KEY_RETROFIT_SETTING)))
            }
        }.asLiveData()

    fun setRetrofitSetting(retrofitSetting: RetrofitSetting) =
        object : IOLiveData<Void>(appExecutors) {
            override fun onExecute() {
                pref.putObjectOrObjectList(KEY_RETROFIT_SETTING, retrofitSetting)
                setValue(Resource.success(null))
            }
        }.asLiveData()
}
