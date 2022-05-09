package com.ibrahim.cryptotracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ibrahim.cryptotracker.databinding.BpiLayoutBinding
import com.ibrahim.cryptotracker.room_db.entities.Currency
import com.ibrahim.cryptotracker.utils.Utils
import javax.inject.Inject

class CurrenciesAdapter @Inject constructor() :
    ListAdapter<Currency, CurrenciesAdapter.CurrencyViewHolder>(DiffCallback()) {

    inner class CurrencyViewHolder(
        private val binding: BpiLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) {
            binding.apply {
                tvCode.text = currency.code
                tvDescription.text = currency.description
                tvSymbol.text = Utils.currencySymbol(currency.code)
                tvRate.text = currency.rate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = BpiLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}

class DiffCallback : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.code == newItem.code
    }
}