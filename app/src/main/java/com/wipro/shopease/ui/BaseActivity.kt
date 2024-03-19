package com.wipro.shopease.ui

import androidx.appcompat.app.AppCompatActivity
import com.wipro.shopease.common.SharedPref
import com.wipro.shopease.domain.repository.RetrofitRepository
import com.wipro.shopease.domain.room.AppDataBase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var appDataBase: AppDataBase

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

}