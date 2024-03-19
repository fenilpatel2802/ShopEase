package com.wipro.shopease.ui.product_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wipro.shopease.R
import com.wipro.shopease.adapter.ImageSlideAdapter
import com.wipro.shopease.common.decimalToString
import com.wipro.shopease.common.mainNav
import com.wipro.shopease.databinding.FragmentProductDetailBinding
import com.wipro.shopease.domain.model.ProductItem
import com.wipro.shopease.ui.BaseFragment

private const val ARG_PRODUCT = "product_detail"

class ProductDetailFragment : BaseFragment() {
    private var productItem: ProductItem? = null

    // binding
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productItem = it.getParcelable(ARG_PRODUCT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initClick()
        initImageSliderAdapter()
    }

    private fun initClick() {
        binding.imgBack.setOnClickListener {
            mainNav.popBackStack()
        }
    }

    private fun initImageSliderAdapter() {
        productItem?.images.let {
            val mSliderAdapter = ImageSlideAdapter(requireActivity(), emptyList(), it)
            binding.bannerSlider.setShowPoint(true)
            binding.bannerSlider.setAdapter(mSliderAdapter)
        }
    }

    private fun initData() {
        binding.tvProductName.text = productItem?.title

        val price = "$${productItem?.price.decimalToString()}"
        binding.tvProductPrice.text = price

        val stock =
            "${getString(R.string.stock_available)} ${productItem?.stock.decimalToString()}"
        binding.tvStockAvailable.text = stock

        binding.tvDetail.text = productItem?.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}