package ru.cactus.currex.ui.common

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseFragment : Fragment {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected abstract val binding: ViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupViews()
        setupObservers()
    }

    open fun setupObservers() {}

    open fun setupViews() {}

    fun dialog(
        title: String,
        message: String,
        btnTextPositive: String? = null
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(btnTextPositive ?: "ОК", null)
            .show()
    }
}