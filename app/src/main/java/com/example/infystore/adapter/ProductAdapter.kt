package com.example.infystore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.infystore.databinding.ItemProductBinding
import com.example.infystore.model.Product
import com.example.infystore.viewmodel.HomeViewModel
import com.example.infystore.BR
import javax.inject.Inject

/**
 * The ProductAdapter class, to create list of items
 * @Inject to provide instance of class
 */
class ProductAdapter @Inject constructor(private val homeViewModel: HomeViewModel) :
    RecyclerView.Adapter<ProductAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(homeViewModel, product[position])
    }

    inner class ItemViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * The bind method, to bind data with an xml file and set up conditional data
         */
        fun bind(homeViewModel: HomeViewModel, product: Product) {
            binding.setVariable(BR.homeViewModel, homeViewModel)
            binding.setVariable(BR.product, product)
            binding.executePendingBindings()
        }

    }

    override fun getItemCount(): Int {
        return product.size
    }

    /**
     * The DiffUtil , A helper class to find the difference between two items
     */
    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return newItem == oldItem
        }
    }

    /**
     * The AsyncListDiffer method, to consume live data and present that data in an Adapter
     */
    private val differ = AsyncListDiffer(this, diffCallback)
    var product: List<Product>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

}