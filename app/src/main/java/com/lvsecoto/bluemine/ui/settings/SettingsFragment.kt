package com.lvsecoto.bluemine.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.repo.utils.statusobserver.hasSuccess
import com.lvsecoto.bluemine.data.vo.DEFAULT_PORT
import com.lvsecoto.bluemine.data.vo.RetrofitSetting
import com.lvsecoto.bluemine.databinding.FragmentSettingsBinding
import com.lvsecoto.bluemine.utils.navigation.popup
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

private const val KEY_RETROFIT_SETTING = "retrofit_settings"

class SettingsFragment : Fragment() {

    companion object {
        fun getInstance(): SettingsFragment =
            SettingsFragment().apply {
                arguments = Bundle()
            }
    }

    private lateinit var dataBinding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    private val gson: Gson by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 当argument内不存在配置信息时，则载入设置一次，实际上这里真相源不是持久层，而是Fragment
        if (!arguments!!.containsKey(KEY_RETROFIT_SETTING)) {
            viewModel.retrofitSetting.hasSuccess(this) { resource ->
                resource.data?.let {
                    saveRetrofitSetting(it)
                    showRetrofitSetting()
                }
                viewModel.retrofitSetting.removeObservers(this)
            }
        } else {
            showRetrofitSetting()
        }

        dataBinding.finish.setOnClickListener {
            viewModel.setRetrofitSetting(
                RetrofitSetting(
                    hostName = dataBinding.hostName.text.toString(),
                    port = dataBinding.port.text.toString().toIntOrNull() ?: 80,
                    userName = dataBinding.userName.text.toString(),
                    password = dataBinding.password.text.toString()
                )
            )
        }
        viewModel.getActionSetRetrofitSetting().hasSuccess(viewLifecycleOwner) {
            popup()
        }

        dataBinding.icBack.setOnClickListener {
            popup()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveRetrofitSetting(
            RetrofitSetting(
                hostName = dataBinding.hostName.text.toString(),
                port = dataBinding.port.text.toString().toIntOrNull() ?: DEFAULT_PORT,
                userName = dataBinding.userName.text.toString(),
                password = dataBinding.password.text.toString()
            )
        )
    }

    private fun showRetrofitSetting() {
        val retrofitSetting =
            arguments!!.getString(KEY_RETROFIT_SETTING)
                .let { gson.fromJson(it, RetrofitSetting::class.java) }
        dataBinding.hostName.setText(retrofitSetting.hostName)
        dataBinding.port.setText(retrofitSetting.port.toString())
        dataBinding.userName.setText(retrofitSetting.userName)
        dataBinding.password.setText(retrofitSetting.password)
    }

    private fun saveRetrofitSetting(retrofitSetting: RetrofitSetting) {
        arguments!!.putString(KEY_RETROFIT_SETTING, gson.toJson(retrofitSetting))
    }
}