package com.wipro.shopease.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext context: Context) {

    private var mSettingPrefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private lateinit var mSettingPrefEditor: SharedPreferences.Editor

    // string
    fun setPrefString(mKey: String, mItem: String) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.putString(mKey, mItem)
        mSettingPrefEditor.commit()
    }

    fun getPrefString(mKey: String): String? {
        return mSettingPrefs.getString(mKey, "")
    }

    // Integer
    fun setPrefInt(mKey: String, mItem: Int) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.putInt(mKey, mItem)
        mSettingPrefEditor.commit()
    }

    fun getPrefInt(mKey: String): Int {
        return mSettingPrefs.getInt(mKey, 0)
    }

    // Boolean
    fun setPrefBoolean(mKey: String, mItem: Boolean) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.putBoolean(mKey, mItem)
        mSettingPrefEditor.commit()
    }

    fun getPrefBoolean(mKeys: String): Boolean {
        return mSettingPrefs.getBoolean(mKeys, false)
    }

    // clear all
    fun clearAllPref() {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.clear()
        mSettingPrefEditor.commit()
    }

    // remove
    fun removePref(mKey: String) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.remove(mKey)
        mSettingPrefEditor.commit()
    }

}