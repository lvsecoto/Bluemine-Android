package com.lvsecoto.bluemine.ui.issuedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.repo.utils.statusobserver.data
import com.lvsecoto.bluemine.databinding.FragmentIssueDetailBinding
import com.lvsecoto.bluemine.databinding.ViewItemIssueDetailBinding
import com.lvsecoto.bluemine.utils.navigation.popup
import com.lvsecoto.bluemine.utils.recyclerview.ConcatAdapter
import com.lvsecoto.bluemine.utils.recyclerview.SingleViewAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class IssueDetailFragment : Fragment() {

    companion object {
        fun getInstance(issueId: Int) : IssueDetailFragment =
                IssueDetailFragment().apply {
                    arguments = bundleOf(
                        "issueId" to issueId
                    )
                }
    }

    private lateinit var binding: FragmentIssueDetailBinding

    private val detailAdapter = SingleViewAdapter<ViewItemIssueDetailBinding>(R.layout.view_item_issue_detail)

    private val attachmentPicAdapter = AttachmentAdapter()

    private val viewModel : IssueDetailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issue_detail, container, false)
        binding.container.layoutManager = LinearLayoutManager(context)
        binding.container.adapter = ConcatAdapter(detailAdapter, attachmentPicAdapter)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.issueId.value = arguments!!.getInt("issueId")

        binding.icBack.setOnClickListener {
            popup()
        }
        viewModel.issueDetail.data(viewLifecycleOwner) {issueDetail ->
            detailAdapter.submit {
                it.issueDetail = issueDetail
            }
            attachmentPicAdapter.submitAttachments(issueDetail?.attachments)
        }
    }
}
