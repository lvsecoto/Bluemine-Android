package com.lvsecoto.bluemine.ui.home.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.repo.utils.errorReport
import com.lvsecoto.bluemine.data.repo.utils.statusobserver.data
import com.lvsecoto.bluemine.databinding.FragmentProjectsBinding
import com.lvsecoto.bluemine.ui.home.HomeViewModel
import com.lvsecoto.bluemine.utils.toast.showToast
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ProjectListFragment : Fragment() {

    companion object {
        fun getInstance() : ProjectListFragment =
                ProjectListFragment().apply {
                    arguments = Bundle()
                }
    }

    private lateinit var binding: FragmentProjectsBinding

    private val viewModel: ProjectListViewModel by viewModel()
    private val hostViewModel: HomeViewModel by sharedViewModel()

    private val projectAdapter = ProjectAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_projects, container, false)
        binding.container.layoutManager = LinearLayoutManager(context)
        binding.container.adapter = projectAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkNotNull(arguments) {
            "please create fragment with setArgument()"
        }
        arguments!!.let {
            projectAdapter.selectedPosition = it.getInt("position", 0)
        }
        projectAdapter.onSelectedPositionChange = { position ->
            arguments!!.putInt("position", position)
        }

        viewModel.projects.errorReport(this)
        viewModel.projects.data(viewLifecycleOwner) {
            projectAdapter.submitProject(it)
        }

        projectAdapter.onClickProject = {
            hostViewModel.actionClickedProjectItem.value = it
        }
        projectAdapter.onSelectedProjectChange = {
            showToast("project is $it?.id")
        }
    }
}
