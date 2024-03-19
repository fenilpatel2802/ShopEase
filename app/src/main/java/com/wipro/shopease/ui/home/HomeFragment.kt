package com.wipro.shopease.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wipro.shopease.R
import com.wipro.shopease.adapter.CategoryRecyclerAdapter
import com.wipro.shopease.adapter.ImageSlideAdapter
import com.wipro.shopease.adapter.ProductRecyclerAdapter
import com.wipro.shopease.common.Const.PREF_EMAIL
import com.wipro.shopease.common.Const.PREF_FIRST_NAME
import com.wipro.shopease.common.Const.PREF_IMAGE
import com.wipro.shopease.common.loadImage
import com.wipro.shopease.common.mainNav
import com.wipro.shopease.databinding.FragmentHomeBinding
import com.wipro.shopease.ui.BaseFragment
import com.wipro.shopease.ui.MainActivity
import com.wipro.shopease.utils.Resource

class HomeFragment : BaseFragment() {

    // binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: HomeViewModel by viewModels()

    // adapter
    private lateinit var mProductAdapter: ProductRecyclerAdapter
    private lateinit var mCategoryAdapter: CategoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        // adapter
        initImageSliderAdapter()
        initProductAdapter()
        initCategoryAdapter()

        // Api call
        if (viewModel.responseProduct.value?.data == null) {
            initProductObserver()
            viewModel.productApiCall()
        }
        if (viewModel.responseCategory.value?.data == null) {
            initCategoryObserver()
            viewModel.categoryApiCall()
        }
    }

    private fun initProductAdapter() {
        // product adapter
        mProductAdapter = ProductRecyclerAdapter(viewModel.productList).apply {
            mClickObserver.observe(viewLifecycleOwner) {
                val args = Bundle().apply {
                    putParcelable("product_detail", viewModel.productList[it])
                }
                mainNav.navigate(R.id.action_homeFragment_to_productDetailFragment, args)
            }
        }
        binding.rvProduct.apply {
            this.adapter = mProductAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)

        }
    }

    private fun initCategoryAdapter() {
        mCategoryAdapter = CategoryRecyclerAdapter(viewModel.categoryList).apply {
            mClickObserver.observe(viewLifecycleOwner) {
                (activity as MainActivity).mainActivityBinding.drawerLayout.closeDrawer(
                    GravityCompat.START
                )
                val args = Bundle().apply {
                    putString("category_name", viewModel.categoryList[it])
                }
                mainNav.navigate(R.id.action_homeFragment_to_productFragment, args)
            }
        }
        with((activity as MainActivity).mainActivityBinding.layoutHeader) {
            rvCategory.apply {
                this.adapter = mCategoryAdapter
                this.layoutManager = LinearLayoutManager(requireContext())
            }
        }
        // category adapter
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

    private fun initCategoryObserver() {
        viewModel.responseCategory.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        Log.e("TAG", "---->> category: initObserver: $it")
                        it.let { it1 ->
                            viewModel.categoryList = it1?.toMutableList()!!
                            mCategoryAdapter.addNewData(it1)
                        }
                    }
                }

                Resource.Status.ERROR -> {}

                Resource.Status.LOADING -> {}
            }
        }
    }

    private fun initData() {
        val title = "${getString(R.string.hello)} ${sharedPref.getPrefString(PREF_FIRST_NAME)}"
        binding.tvTitle.text = title
        with((activity as MainActivity).mainActivityBinding.layoutHeader) {
            imgProfile.loadImage(sharedPref.getPrefString(PREF_IMAGE) ?: "")
            tvEmail.text = sharedPref.getPrefString(PREF_EMAIL) ?: ""
            tvUserName.text = sharedPref.getPrefString(PREF_FIRST_NAME) ?: ""
        }
    }

    private fun initImageSliderAdapter() {
        val mSliderAdapter = ImageSlideAdapter(
            requireActivity(),
            viewModel.imageSliderList(),
            emptyList()
        )
        binding.bannerSlider.setShowPoint(true)
        binding.bannerSlider.setAdapter(mSliderAdapter)
        binding.bannerSlider.setIntervalTime(6000)
        binding.bannerSlider.setAutoScrollDurationFactor(8.0)
        binding.bannerSlider.setSwipeScrollDurationFactor(1.0)
        binding.bannerSlider.setMarginPadding(40, 20, 40, 10, 25)
        binding.bannerSlider.startSlide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}