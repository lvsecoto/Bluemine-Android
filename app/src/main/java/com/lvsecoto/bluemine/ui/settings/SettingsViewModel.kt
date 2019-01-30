package com.lvsecoto.bluemine.ui.settings

import androidx.lifecycle.ViewModel
import com.lvsecoto.bluemine.data.repo.SettingsRepository
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.vo.RetrofitSetting
import com.lvsecoto.bluemine.utils.ActionLiveData

class SettingsViewModel(
    private val repo: SettingsRepository
) : ViewModel() {

    val retrofitSetting = repo.getRetrofitSetting()

    private val actionSetRetrofitSetting = ActionLiveData.create<RetrofitSetting, Resource<Void>> {
        repo.setRetrofitSetting(it)
    }

    fun setRetrofitSetting(retrofitSetting: RetrofitSetting) {
        actionSetRetrofitSetting.input(retrofitSetting)
    }

    fun getActionSetRetrofitSetting() =
            actionSetRetrofitSetting.asLiveData()
}