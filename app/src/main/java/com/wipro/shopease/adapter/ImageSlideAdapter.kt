package com.wipro.shopease.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.wipro.shopease.R
import com.wipro.shopease.common.Slider.AutoSlideViewPageAdapter
import com.wipro.shopease.common.loadImage

class ImageSlideAdapter(
    private var activity: Activity,
    private var mBanners: List<Int>,
    private var mProductImage: List<String?>?
) :
    AutoSlideViewPageAdapter() {
    override fun getCurrentPageTitle(i: Int): CharSequence {
        return "Current Page $i"
    }

    override fun getPageCount(): Int {
        return if (mBanners.isEmpty()) {
            mProductImage!!.size
        } else {
            mBanners.size
        }
    }

    override fun instantiatePageItem(i: Int): View {
        val view =
            (activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.slider_home_image,
                null
            )
        val mImageView = view.findViewById<ImageView>(R.id.image_display)
        if (mBanners.isEmpty()) {
            mProductImage?.get(i)?.let { mImageView.loadImage(it) }
        } else {
            mImageView.setImageResource(mBanners[i])
        }
        return view
    }
}
