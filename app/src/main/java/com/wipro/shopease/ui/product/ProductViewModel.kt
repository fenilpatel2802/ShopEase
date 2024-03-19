package com.wipro.shopease.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wipro.shopease.domain.model.ProductItem
import com.wipro.shopease.domain.model.ProductResponse
import com.wipro.shopease.domain.repository.RetrofitRepository
import com.wipro.shopease.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: RetrofitRepository
) : ViewModel() {
    // product
    private val _responseProduct: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val responseProduct: LiveData<Resource<ProductResponse>> = _responseProduct
    val productList: MutableList<ProductItem?> = mutableListOf()

    // product api call
    fun productApiCall(categoryName: String?) = viewModelScope.launch {
        _responseProduct.postValue(Resource.loading())

        repository.getCategoryWiseProductList(categoryName).collect { values ->
            _responseProduct.value = values
        }
    }


}