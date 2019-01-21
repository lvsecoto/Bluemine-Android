package com.lvsecoto.bluemine

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lvsecoto.bluemine.ui.home.IssuesListFragment
import com.lvsecoto.bluemine.utils.navigation.NavigationActivity

class MainActivity : NavigationActivity() {
    override val startFragment: Fragment
        get() = IssuesListFragment()

    override fun navigateTo(action: String, bundle: Bundle?) {
    }
}
