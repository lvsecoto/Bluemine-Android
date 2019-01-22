package com.lvsecoto.bluemine.ui.home.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.databinding.FragmentProjectsBinding

class ProjectListFragment : Fragment() {

    private lateinit var binding : FragmentProjectsBinding

    private val projectAdapter = ProjectAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_projects, container, false)
        binding.container.layoutManager = LinearLayoutManager(context)
        binding.container.adapter = projectAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectAdapter.submitDemos(arrayListOf(
            "测试项目",
            "蓝石头 安卓端",
            "蓝石头 IOS端",
            "蓝石头 小程序",
            "蓝石头 OSX"
        ))
    }
}
