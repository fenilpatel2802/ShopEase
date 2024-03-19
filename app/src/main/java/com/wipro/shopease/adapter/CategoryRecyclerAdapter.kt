package com.wipro.shopease.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.wipro.shopease.databinding.LayoutCategoryBinding

class CategoryRecyclerAdapter(private var list: MutableList<String?>) :
    RecyclerView.Adapter<CategoryRecyclerAdapter.MyHolder>() {

    val mClickObserver = MutableLiveData<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryRecyclerAdapter.MyHolder {
        val binding =
            LayoutCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryRecyclerAdapter.MyHolder, position: Int) {
        with(holder.binding) {
            list[position].let {
                tvCategory.text = it
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addNewData(newList: List<String>) {
        val startingListCount = list.size
        list.addAll(newList)
        notifyItemRangeInserted(startingListCount, newList.size)
    }

    inner class MyHolder(val binding: LayoutCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                mClickObserver.postValue(adapterPosition)
            }
        }
    }
}