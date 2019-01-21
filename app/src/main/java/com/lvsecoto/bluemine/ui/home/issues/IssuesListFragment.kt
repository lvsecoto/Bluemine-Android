package com.lvsecoto.bluemine.ui.home.issues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.databinding.FragmentIssuesListBinding
import com.lvsecoto.bluemine.utils.showToast

class IssuesListFragment : Fragment() {

    lateinit var binding : FragmentIssuesListBinding

    private val issuesAdapter = IssuesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issues_list, container, false)
        binding.issues.layoutManager = LinearLayoutManager(context)
        binding.issues.adapter = issuesAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        issuesAdapter.submitDemo(arrayListOf(
            "123",
            "456",
            "789",
            "123",
            "456",
            "789",
            "123",
            "456",
            "789"
        ))
        issuesAdapter.onClickItem = {
            showToast("click position $it!")
        }
    }

}
