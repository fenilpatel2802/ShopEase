package com.wipro.shopease.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.wipro.shopease.R
import com.wipro.shopease.adapter.ProductRecyclerAdapter
import com.wipro.shopease.common.mainNav
import com.wipro.shopease.databinding.FragmentProductBinding
import com.wipro.shopease.ui.BaseFragment
import com.wipro.shopease.ui.MainActivity
import com.wipro.shopease.utils.Resource

private const val ARG_PARAM1 = "category_name"

class ProductFragment : BaseFragment() {
    private var categoryName: String? = null

    // binding
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: ProductViewModel by viewModels()

    // adapter
    private lateinit var mProductAdapter: ProductRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initProductAdapter()

        // Api call
        if (viewModel.responseProduct.value?.data == null) {
            // Observer
            initProductObserver()
            viewModel.productApiCall(categoryName)
        }
    }

    private fun initData() {
        (activity as MainActivity).mainActivityBinding.toolbar.tvTitle.text = categoryName
    }

    private fun initProductAdapter() {
        // product adapter
        mProductAdapter = ProductRecyclerAdapter(viewModel.productList).apply {
            mClickObserver.observe(viewLifecycleOwner) {
                val args = Bundle().apply {
                    putParcelable("product_detail", viewModel.productList[it])
                }
                mainNav.navigate(R.id.action_productFragment_to_productDetailFragment, args)
            }
        }
        binding.rvProduct.apply {
            this.adapter = mProductAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)

        }
    }

    private fun initProductObserver() {
        viewModel.responseProduct.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        Log.e("TAG", "---->> " + "initObserver: ${it?.results}")
                        it?.results?.let { it1 ->
                            val startingListCount = viewModel.productList.size
                            viewModel.productList.addAll(it1)
                            mProductAdapter.notifyItemRangeInserted(startingListCount, it1.size)
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}