package com.lvsecoto.bluemine.ui.home.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.network.response.ProjectResponse
import com.lvsecoto.bluemine.data.network.utils.ApiErrorResponse
import com.lvsecoto.bluemine.data.network.utils.ApiSuccessResponse
import com.lvsecoto.bluemine.databinding.FragmentProjectsBinding
import com.lvsecoto.bluemine.utils.toast.showToast
import org.koin.android.viewmodel.ext.android.viewModel

class ProjectListFragment : Fragment() {

    private lateinit var binding : FragmentProjectsBinding

    private val projectAdapter = ProjectAdapter()

    private val viewModel : ProjectListViewModel by viewModel()

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
        viewModel.projects.observe(this, Observer {
            if (it is ApiSuccessResponse<ProjectResponse>) {
                showToast("${it.body}")
            } else if (it is ApiErrorResponse<ProjectResponse>) {
                showToast(it.errorMessage)
            }
        })
    }
}
