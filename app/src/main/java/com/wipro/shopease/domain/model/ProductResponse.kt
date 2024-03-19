package com.wipro.shopease.domain.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProductResponse(

	@field:SerializedName("products")
	val results: List<ProductItem?>? = null

) : Parcelable