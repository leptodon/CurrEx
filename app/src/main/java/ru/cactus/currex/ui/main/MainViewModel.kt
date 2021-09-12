package ru.cactus.currex.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.cactus.currex.data.repository.CurrencyRepository
import ru.cactus.currex.data.response_models.ValCurs
import ru.cactus.currex.data.response_models.Valute
import ru.cactus.currex.domain.SelectedValue
import ru.cactus.currex.formatToShortString

class MainViewModel(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    companion object {
        const val RUB_ID = "111"
        const val RUB_NUM_CODE = 111
        const val RUB_CHAR_CODE = "RUB"
        const val RUB_NOMINAL = 1
        const val RUB_NAME = "Российский рубль"
        const val RUB_VALUE = "1"
    }

    var selectedValue: SelectedValue = SelectedValue.FIRST
    private var _isProgress = MutableLiveData<Boolean>()
    val isProgress: LiveData<Boolean> = _isProgress

    var valCurs: ValCurs? = null
        set(value) {
            field = value
            value ?: return
            val firstValue = value.valuteList?.get(0)?.apply { isSelect = true }
            val secondValue = value.valuteList?.get(1)?.apply { isSelect = true }
            _firstValute.value = firstValue
            _secondValute.value = secondValue

            _firstValue.value = firstValue?.value ?: ""
            _secondValue.value = secondValue?.value ?: ""
        }

    private val _firstValue = MutableLiveData<String?>()
    val firstValue: LiveData<String?> = _firstValue

    private val _secondValue = MutableLiveData<String?>()
    val secondValue: LiveData<String?> = _secondValue

    private val _firstValute = MutableLiveData<Valute?>()
    val firstValute: LiveData<Valute?> = _firstValute

    private val _secondValute = MutableLiveData<Valute?>()
    val secondValute: LiveData<Valute?> = _secondValute

    var firstOrSecondInput: Boolean? = null
        set(value) {
            field = value
            value ?: return
            selectedValue = if (value) SelectedValue.FIRST else SelectedValue.SECOND
        }

    var firstValuteCurs: String = ""
        set(value) {
            field = value
            firstOrSecondInput?.let {
                if (it) {
                    _secondValue.value =
                        calculate(
                            value
                                .replace(",", ".")
                                .toFloatOrNull()
                        )?.formatToShortString(2)
                }
            }

        }
    var secondValuteCurs: String = ""
        set(value) {
            field = value
            firstOrSecondInput?.let {
                if (!it) {
                    _firstValue.value =
                        calculate(
                            value
                                .replace(",", ".")
                                .toFloatOrNull()
                        )?.formatToShortString(2)
                }
            }

        }

    init {
        loadCurrency()
    }

    private fun loadCurrency() = viewModelScope.launch {
        currencyRepository.getCurrency()
            .onStart { _isProgress.postValue(true) }
            .catch {
                Log.d("MainViewModel", it.message ?: it.message.toString())
            }
            .collect { response ->
                response ?: return@collect
                val listWithRub = response.valuteList?.toMutableList()
                listWithRub?.add(
                    0, Valute(
                        id = RUB_ID,
                        numCode = RUB_NUM_CODE,
                        charCode = RUB_CHAR_CODE,
                        nominal = RUB_NOMINAL,
                        name = RUB_NAME,
                        value = RUB_VALUE
                    )
                )
                response.valuteList = listWithRub
                valCurs = response
                _isProgress.postValue(false)
            }
    }

    fun showIsNewValute(selectedValue: SelectedValue, valute: Valute) {
        when (selectedValue) {
            SelectedValue.FIRST -> {
                _firstValute.value = valute
                _secondValue.value =
                    calculate(
                        firstValuteCurs
                            .replace(",", ".")
                            .toFloatOrNull()
                    )?.formatToShortString(2)
            }
            SelectedValue.SECOND -> {
                _secondValute.value = valute
                _firstValue.value =
                    calculate(
                        secondValuteCurs
                            .replace(",", ".")
                            .toFloatOrNull()
                    )?.formatToShortString(2)
            }
        }
    }

    private fun calculate(amount: Float?): Float? {
        amount ?: return null
        val firstValute = _firstValute.value ?: return null
        val secondValute = _secondValute.value ?: return null
        Log.d("TAG", "calculate")
        return when {
            firstValute.numCode != RUB_NUM_CODE || secondValute.numCode != RUB_NUM_CODE -> {
                val numerator =
                    firstValute.value?.replace(",", ".")?.toFloat()
                        ?.div(firstValute.nominal?.toFloat() ?: return null)
                val denominator = secondValute.value?.replace(",", ".")?.toFloat()
                    ?.div(secondValute.nominal?.toFloat() ?: return null)

                when (selectedValue) {
                    SelectedValue.FIRST -> (numerator?.div(denominator!!))?.times(
                        amount
                    )
                    SelectedValue.SECOND -> (denominator?.div(numerator!!))?.times(
                        amount
                    )
                }
            }
            firstValute.numCode == RUB_NUM_CODE || secondValute.numCode == RUB_NUM_CODE -> {
                when (selectedValue) {
                    SelectedValue.FIRST -> (secondValute.nominal?.toFloat()
                        ?.div(
                            secondValute.value?.replace(",", ".")?.toFloat() ?: return null
                        ))?.times(amount)
                    SelectedValue.SECOND -> (firstValute.value?.replace(
                        ",",
                        "."
                    )?.toFloat()?.div(firstValute.nominal!!.toFloat()))?.times(amount)
                }
            }
            else -> null
        }
    }
}