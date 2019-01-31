package com.lvsecoto.bluemine.data.repo

import android.content.SharedPreferences
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.db.AppDao
import com.lvsecoto.bluemine.data.cache.utils.objectLiveData
import com.lvsecoto.bluemine.data.cache.utils.putObjectOrObjectList
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.repo.utils.toSuccess
import com.lvsecoto.bluemine.data.vo.RetrofitSetting
import com.lvsecoto.bluemine.utils.IOLiveData

private const val KEY_RETROFIT_SETTING = "KEY_RETROFIT_SETTING"

/**
 * 单例
 */
class SettingsRepository(
    private val appExecutors: AppExecutors,
    private val pref: SharedPreferences,
    private val appDao: AppDao
) {
    var retrofitSetting: RetrofitSetting? = null

    @Suppress("unused")
    private val retrofitSettingSource =
        pref.objectLiveData<RetrofitSetting>(KEY_RETROFIT_SETTING).apply {
        observeForever {
            retrofitSetting = it
        }
    }

    fun getRetrofitSetting() =
        pref.objectLiveData<RetrofitSetting>(KEY_RETROFIT_SETTING).toSuccess(appExecutors)

    fun setRetrofitSetting(retrofitSetting: RetrofitSetting) =
        object : IOLiveData<Void>(appExecutors) {
            override fun onExecute() {
                appDao.clearRetrofitData()
                pref.putObjectOrObjectList(KEY_RETROFIT_SETTING, retrofitSetting)
                setValue(Resource.success(null))
            }
        }.asLiveData()
}
