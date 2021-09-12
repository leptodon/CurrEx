package ru.cactus.currex.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import ru.cactus.currex.data.response_models.Valute
import ru.cactus.currex.databinding.ItemCurrencyBinding

class CurrencyAdapter(
    private val onItemClick: (Valute) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private val items = mutableListOf<Valute>()

    @SuppressLint("NotifyDataSetChanged")
    fun showList(list: List<Valute>) {
        items.clear()
        items.addAll(list)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemBinding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClick)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Valute, onItemClick: (Valute) -> Unit) {
            with(binding) {
                tvName.text = data.name
                tvCharCode.text = data.charCode
                ivCheck.isInvisible = !data.isSelect

                root.setOnClickListener {
                    onItemClick.invoke(data)
                }
            }
        }
    }
}