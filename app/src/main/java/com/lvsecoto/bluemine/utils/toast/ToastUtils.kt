package com.lvsecoto.bluemine.utils.toast

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(toast: CharSequence) {
    Toast.makeText(this.context, toast, Toast.LENGTH_LONG).show()
}