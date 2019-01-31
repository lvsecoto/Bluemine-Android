package com.lvsecoto.bluemine.ui.home.project

import androidx.recyclerview.widget.DiffUtil
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.vo.Project
import com.lvsecoto.bluemine.databinding.ViewItemProjectBinding
import com.lvsecoto.bluemine.utils.recyclerview.DataBoundListAdapter

class ProjectAdapter : DataBoundListAdapter<ProjectAdapter.Wrapper, ViewItemProjectBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Wrapper>() {
        override fun areItemsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem == newItem
    }
) {
    var onClickProject: ((Project) -> Unit)? = null

    var onSelectedPositionChange: ((Int) -> Unit)? = null
    var onSelectedProjectChange: ((Project?) -> Unit)? = null

    /**
     * [selectedPosition]不一定在列表范围内
     */
    var selectedPosition: Int = 0
        set(newPosition) {
            if (newPosition != field) {
                notifyItemChanged(newPosition)
                notifyItemChanged(field)

                field = newPosition

                onSelectedPositionChange?.invoke(newPosition)
            }
        }

    /**
     * 每当[selectedPosition]和列表的数据间关系发生变化时，都应当更新[selectedProject]
     */
    private var selectedProject: Project? = null
        set(newProject) {
            if (field != newProject) {
                field = newProject

                onSelectedProjectChange?.invoke(newProject)
            }
        }

    override val layoutId: Int
        get() = R.layout.view_item_project

    override fun onBindData(dataBinding: ViewItemProjectBinding, item: Wrapper, position: Int) {
        super.onBindData(dataBinding, item, position)

        dataBinding.isSelected = position == selectedPosition
        dataBinding.root.setOnClickListener {
            onClickProject?.invoke(item.project)

            selectedPosition = position
            selectedProject = getProjectOrNull(position)
        }
    }

    fun submitProject(demos: List<Project>?) {
        submitList((demos ?: emptyList()).map { Wrapper(it) })
        selectedProject = getProjectOrNull(selectedPosition)
    }

    private fun getProjectOrNull(position: Int): Project? {
        return if (position in 0 until itemCount) {
            getItem(position).project
        } else {
            null
        }
    }

    fun clearSelectedProject() {
        selectedProject = null
    }

    fun clearAndSelectProject(position: Int) {
        selectedProject = null
        selectedPosition = position
        selectedProject = getProjectOrNull(selectedPosition)
    }

    data class Wrapper(val project: Project) {
        val name = project.name
    }
}
