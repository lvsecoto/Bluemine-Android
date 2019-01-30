package com.lvsecoto.bluemine.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.databinding.FragmentSettingsBinding
import com.lvsecoto.bluemine.utils.navigation.popup

class SettingsFragment : Fragment() {

    companion object {
        fun getInstance() : SettingsFragment =
            SettingsFragment()
    }

    private lateinit var dataBinding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.icBack.setOnClickListener {
            popup()
        }
        dataBinding.finish.setOnClickListener {
            popup()
        }
    }
}