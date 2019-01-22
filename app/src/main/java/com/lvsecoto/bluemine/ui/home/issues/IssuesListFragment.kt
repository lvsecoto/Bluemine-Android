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
import com.lvsecoto.bluemine.databinding.FragmentIssuesListBinding
import com.lvsecoto.bluemine.ui.home.project.ProjectListFragment
import com.lvsecoto.bluemine.utils.showToast

class IssuesListFragment : Fragment() {

    private lateinit var binding: FragmentIssuesListBinding

    private val issuesAdapter = IssuesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issues_list, container, false)

        binding.issues.layoutManager = LinearLayoutManager(context)
        binding.issues.adapter = issuesAdapter

        binding.icMenu.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }
        setupProjectsDrawer()

        return binding.root
    }

    private fun setupProjectsDrawer() {
        if (fragmentManager!!.findFragmentById(binding.projects.id) == null) {

            fragmentManager!!.commit {
                replace(binding.projects.id, ProjectListFragment())
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        issuesAdapter.submitDemo(
            arrayListOf(
                "123",
                "456",
                "789",
                "123",
                "456",
                "789",
                "123",
                "456",
                "789"
            )
        )
        issuesAdapter.onClickItem = {
            showToast("click position $it!")
        }
    }

}
