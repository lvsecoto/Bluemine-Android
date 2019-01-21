package com.lvsecoto.bluemine.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.databinding.FragmentIssuesListBinding

class IssuesListFragment : Fragment() {

    lateinit var binding : FragmentIssuesListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issues_list, container, false)
        return binding.root
    }

}
