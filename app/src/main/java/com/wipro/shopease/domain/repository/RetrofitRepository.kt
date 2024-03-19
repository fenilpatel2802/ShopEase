package com.wipro.shopease.domain.repository

import com.demo.mvvm.utils.BaseApiResponse
import com.wipro.shopease.domain.model.LoginResponse
import com.wipro.shopease.domain.model.ProductResponse
import com.wipro.shopease.domain.rest.RetrofitService
import com.wipro.shopease.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val service: RetrofitService
) : BaseApiResponse() {
    // login
    suspend fun login(reqBody: Map<String, String>): Flow<Resource<LoginResponse>> {
        return flow {
            emit(safeApiCall { service.login(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

    // product list
    suspend fun getProductList(): Flow<Resource<ProductResponse>> {
        return flow {
            emit(safeApiCall { service.getProductList() })
        }.flowOn(Dispatchers.IO)
    }

    // category list
    suspend fun getCategoryList(): Flow<Resource<List<String>>> {
        return flow {
            emit(safeApiCall { service.getCategoryList() })
        }.flowOn(Dispatchers.IO)
    }

    // category list
    suspend fun getCategoryWiseProductList(categoryName: String?): Flow<Resource<ProductResponse>> {
        return flow {
            emit(safeApiCall { service.getCategoryWiseProductList(categoryName) })
        }.flowOn(Dispatchers.IO)
    }


}