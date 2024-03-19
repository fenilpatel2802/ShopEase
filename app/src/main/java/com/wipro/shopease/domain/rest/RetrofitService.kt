package com.wipro.shopease.domain.rest

import com.wipro.shopease.domain.model.LoginResponse
import com.wipro.shopease.domain.model.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {
    // login
    @POST("auth/login")
    suspend fun login(@Body reqBody: Map<String, String>): Response<LoginResponse>

    // product list
    @GET("products")
    suspend fun getProductList(): Response<ProductResponse>

    // category list
    @GET("products/categories")
    suspend fun getCategoryList(): Response<List<String>>

    // category wise product list
    @GET("products/category/{category}")
    suspend fun getCategoryWiseProductList(@Path("category") categoryName: String?): Response<ProductResponse>


}