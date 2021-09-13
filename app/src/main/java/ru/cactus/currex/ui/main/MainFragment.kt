package ru.cactus.currex.ui.main

import android.view.View
import androidx.core.widget.doOnTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.cactus.currex.R
import ru.cactus.currex.databinding.MainFragmentBinding
import ru.cactus.currex.domain.SelectedValue
import ru.cactus.currex.ui.common.BaseFragment

class MainFragment : BaseFragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by sharedViewModel()
    override val binding by viewBinding<MainFragmentBinding>()

    override fun setupObservers() {
        super.setupObservers()

        with(viewModel) {
            firstValute.observe(viewLifecycleOwner) {
                binding.tvFirstCharCode.text = it?.charCode
            }
            secondValute.observe(viewLifecycleOwner) {
                binding.tvSecondCharCode.text = it?.charCode
            }
            firstCharCode.observe(viewLifecycleOwner) {
                binding.etFirstCur.setText(it ?: "")
            }
            secondCharCode.observe(viewLifecycleOwner) {
                binding.etSecondCur.setText(it ?: "")
            }
            isProgress.observe(viewLifecycleOwner) {
                binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
            }
        }
    }

    override fun setupViews() {
        super.setupViews()

        with(binding) {
            includeToolbar.toolbar.title = getString(R.string.currency_converter)

            etFirstCur.doOnTextChanged { text, _, _, _ ->
                viewModel.firstOrSecondInput = if (!etFirstCur.isFocused) null else true
                val value = text?.toString() ?: return@doOnTextChanged
                viewModel.firstValuteCurs = value
            }

            etSecondCur.doOnTextChanged { text, _, _, _ ->
                viewModel.firstOrSecondInput = if (!etSecondCur.isFocused) null else false
                val value = text?.toString() ?: return@doOnTextChanged
                viewModel.secondValuteCurs = value
            }

            tvChangeFirstCurrency.setOnClickListener {
                showDialog(SelectedValue.FIRST)
            }

            tvChangeSecondCurrency.setOnClickListener {
                showDialog(SelectedValue.SECOND)
            }
        }
    }

    private fun showDialog(selectedValue: SelectedValue) {
        CurrencyDialogFragment(selectedValue).show(childFragmentManager, null)
    }
}
