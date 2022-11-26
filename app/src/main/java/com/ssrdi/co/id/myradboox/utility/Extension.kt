package com.ssrdi.co.id.myradboox.utility

import android.content.Context
import androidx.fragment.app.Fragment


fun Fragment.checkIfFragmentAttached(operation: Context.() -> Unit) {
    if (isAdded && context != null) {
        operation(requireContext())
    }
}