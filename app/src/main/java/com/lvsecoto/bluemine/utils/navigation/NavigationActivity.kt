package com.lvsecoto.bluemine.utils.navigation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.lvsecoto.bluemine.R

abstract class NavigationActivity : AppCompatActivity(), NavigationController {

    override fun onCreate(savedInstanceState: Bundle?) {
        configureWindow()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        initStartFragment()
    }

    /**
     * 设置为全屏
     */
    private fun configureWindow() {
        window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.decorView.requestApplyInsets()
        }
    }

    private fun initStartFragment() {
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.container)

        if (fragment == null) {
            fragment = startFragment
            fm.commit {
                add(R.id.container, fragment)
            }
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        val isStateSaved = fm.isStateSaved
        if (!isStateSaved || Build.VERSION.SDK_INT > 25) {
            if (isStateSaved || !fm.popBackStackImmediate()) {
                // Fragment 回退栈为空
                if (moveTaskToBack(false)) {
                    // 最后一个Activity了，而且应用已经移到后台了，再执行就会关闭应用
                } else {
                    // 不是最后一个，执行原版方法
                    super.onBackPressed()
                }
            }
        }
    }

    /**
     * 返回到最初的Fragment
     */
    protected fun returnToStartFragment() {
        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    /**
     * 初始Fragment
     */
    protected abstract val startFragment: Fragment

    /**
     * 导航至下一个Fragment
     */
    protected fun navigateToAndAddToBackStack(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.container, fragment)
            addToBackStack(null)
        }
    }

    /**
     * 导航不带带参数时，传入null
     */
    override fun navigateTo(action: String) {
        navigateTo(action, null)
    }
}
