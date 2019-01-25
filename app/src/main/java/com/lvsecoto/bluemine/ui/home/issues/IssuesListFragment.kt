package com.lvsecoto.bluemine.ui.home.issues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.repo.utils.errorReport
import com.lvsecoto.bluemine.data.repo.utils.statusobserver.data
import com.lvsecoto.bluemine.databinding.FragmentIssuesListBinding
import com.lvsecoto.bluemine.ui.home.project.ProjectListFragment
import com.lvsecoto.bluemine.utils.navigation.navigation
import org.koin.android.viewmodel.ext.android.viewModel

const val ACTION_VIEW_ISSUE_DETAIL = "ACTION_VIEW_ISSUE_DETAIL"

class IssuesListFragment : Fragment() {

    private lateinit var binding: FragmentIssuesListBinding

    private val viewModel: IssuesViewModel by viewModel()

    private val issuesAdapter = IssuesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issues_list, container, false)

        binding.issues.layoutManager = LinearLayoutManager(context)
        binding.issues.adapter = issuesAdapter

        setupProjectDrawer()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        issuesAdapter.onClickItem = {
            navigation.navigateTo(ACTION_VIEW_ISSUE_DETAIL)
        }
        viewModel.issues.errorReport(this)
        viewModel.issues.data(this) {
            issuesAdapter.submitIssues(it)
        }
    }

    private fun setupProjectDrawer() {
        binding.icMenu.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }
        setupProjectsDrawerFragment()
    }

    private fun setupProjectsDrawerFragment() {
        var fragment = childFragmentManager.findFragmentById(binding.projects.id)
        if (fragment == null) {
            fragment = ProjectListFragment()
            childFragmentManager.commit {
                replace(binding.projects.id, fragment)
            }
        }
    }

}
