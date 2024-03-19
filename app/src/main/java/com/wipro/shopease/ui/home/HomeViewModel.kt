package com.wipro.shopease.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wipro.shopease.R
import com.wipro.shopease.domain.model.ProductItem
import com.wipro.shopease.domain.model.ProductResponse
import com.wipro.shopease.domain.repository.RetrofitRepository
import com.wipro.shopease.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RetrofitRepository
) : ViewModel() {
    // product
    private val _responseProduct: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val responseProduct: LiveData<Resource<ProductResponse>> = _responseProduct
    val productList: MutableList<ProductItem?> = mutableListOf()

    // category
    private val _responseCategory: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val responseCategory: LiveData<Resource<List<String>>> = _responseCategory
    var categoryList: MutableList<String?> = mutableListOf()

    // image slider (Banner)
    fun imageSliderList(): List<Int> {
        return listOf(
            R.drawable.img_intro1,
            R.drawable.img_intro2,
            R.drawable.img_intro3
        )
    }
    // product api call
    fun productApiCall() = viewModelScope.launch {
        _responseProduct.postValue(Resource.loading())

        repository.getProductList().collect { values ->
            _responseProduct.value = values
        }
    }
    // category api call
    fun categoryApiCall() = viewModelScope.launch {
        _responseCategory.postValue(Resource.loading())

        repository.getCategoryList().collect { values ->
            _responseCategory.value = values
        }
    }


}