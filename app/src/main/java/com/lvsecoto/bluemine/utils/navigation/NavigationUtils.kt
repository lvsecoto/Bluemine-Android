package com.lvsecoto.bluemine.utils.navigation

import androidx.fragment.app.Fragment

val Fragment.navigation
    get() = this.activity as NavigationController

fun Fragment.popup() {
    this.activity?.onBackPressed()
}