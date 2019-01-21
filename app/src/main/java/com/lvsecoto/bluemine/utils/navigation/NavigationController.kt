package com.lvsecoto.bluemine.utils.navigation

import android.os.Bundle

interface NavigationController {
    fun navigateTo(action: String)

    fun navigateTo(action: String, bundle: Bundle?)
}
