package com.lvsecoto.bluemine

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lvsecoto.bluemine.ui.home.issues.ACTION_SETTINGS
import com.lvsecoto.bluemine.ui.home.issues.ACTION_VIEW_ISSUE_DETAIL
import com.lvsecoto.bluemine.ui.home.issues.IssuesListFragment
import com.lvsecoto.bluemine.ui.issuedetail.IssueDetailFragment
import com.lvsecoto.bluemine.ui.settings.SettingsFragment
import com.lvsecoto.bluemine.utils.navigation.NavigationActivity

class MainActivity : NavigationActivity() {
    override val startFragment: Fragment
        get() = IssuesListFragment()

    override fun navigateTo(action: String, bundle: Bundle?) {
        when(action) {
            ACTION_VIEW_ISSUE_DETAIL -> navigateToAndAddToBackStack(
                IssueDetailFragment.getInstance(bundle!!.getInt("issueId"))
            )
            ACTION_SETTINGS -> navigateToAndAddToBackStack(
                SettingsFragment.getInstance()
            )
        }
    }
}
