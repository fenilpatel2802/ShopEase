package com.wipro.shopease.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.wipro.shopease.common.decimalToString
import com.wipro.shopease.common.loadImage
import com.wipro.shopease.databinding.LayoutProductBinding
import com.wipro.shopease.domain.model.ProductItem
import java.text.DecimalFormat

class ProductRecyclerAdapter(private var list: MutableList<ProductItem?>?) :
    RecyclerView.Adapter<ProductRecyclerAdapter.MyHolder>() {

    val mClickObserver = MutableLiveData<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductRecyclerAdapter.MyHolder {
        val binding =
            LayoutProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductRecyclerAdapter.MyHolder, position: Int) {
        with(holder.binding) {
            list!![position]?.title.let {
                tvProductName.text = it
            }
            list!![position]?.price.let {
                val price = "$${it.decimalToString()}"
                tvProductPrice.text = price
            }
            list!![position]?.thumbnail.let {
                imgProduct.loadImage(it ?: "")
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class MyHolder(val binding: LayoutProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                mClickObserver.postValue(adapterPosition)
            }
        }
    }
}