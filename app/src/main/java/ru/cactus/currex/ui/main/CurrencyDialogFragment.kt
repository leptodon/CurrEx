package ru.cactus.currex.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.cactus.currex.R
import ru.cactus.currex.data.response_models.Valute
import ru.cactus.currex.databinding.CurrencyDialogBinding
import ru.cactus.currex.domain.SelectedValue
import ru.cactus.currex.ui.main.adapter.CurrencyAdapter

class CurrencyDialogFragment(
    private val selectedValue: SelectedValue,
) : DialogFragment() {

    private val binding by viewBinding<CurrencyDialogBinding>()
    private val viewModel by sharedViewModel<MainViewModel>()

    private val adapter = CurrencyAdapter {
        viewModel.showIsNewValute(selectedValue, it)

        showList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        isCancelable = false
        this.dialog?.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.currency_dialog, container, false)
    }

    override fun getTheme(): Int {
        return R.style.Theme_CurrEx
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            includeToolbar.toolbar.title = getString(R.string.select_currency)
            includeToolbar.tvCancel.isVisible = true
            includeToolbar.tvCancel.setOnClickListener {
                dismiss()
            }

            currencyList.adapter = adapter

            showList()
        }
    }

    private fun showList() {
        adapter.showList(prepareList())
    }

    private fun prepareList(): List<Valute> {
        return when (selectedValue) {
            SelectedValue.FIRST -> {
                val currentList = viewModel.valCurs?.valuteList?.toMutableList() ?: return emptyList()
                currentList.forEach {
                    it.isSelect = it.id == viewModel.firstValute.value?.id
                }
                currentList
            }
            SelectedValue.SECOND -> {
                val currentList = viewModel.valCurs?.valuteList?.toMutableList() ?: return emptyList()
                currentList.forEach {
                    it.isSelect = it.id == viewModel.secondValute.value?.id
                }
                currentList
            }
        }
    }
}