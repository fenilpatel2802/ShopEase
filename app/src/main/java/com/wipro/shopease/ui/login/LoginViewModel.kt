package com.wipro.shopease.ui.login

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wipro.shopease.R
import com.wipro.shopease.common.Const.PREF_EMAIL
import com.wipro.shopease.common.Const.PREF_FIRST_NAME
import com.wipro.shopease.common.Const.PREF_GENDER
import com.wipro.shopease.common.Const.PREF_ID
import com.wipro.shopease.common.Const.PREF_IMAGE
import com.wipro.shopease.common.Const.PREF_IS_LOGIN
import com.wipro.shopease.common.Const.PREF_LAST_NAME
import com.wipro.shopease.common.Const.PREF_TOKEN
import com.wipro.shopease.common.Const.PREF_USER_NAME
import com.wipro.shopease.common.SharedPref
import com.wipro.shopease.common.Validation
import com.wipro.shopease.databinding.FragmentLoginBinding
import com.wipro.shopease.domain.model.LoginResponse
import com.wipro.shopease.domain.repository.RetrofitRepository
import com.wipro.shopease.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: RetrofitRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    private val _response: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val response: LiveData<Resource<LoginResponse>> = _response

    fun saveInPref(response: LoginResponse?) {
        sharedPref.setPrefInt(PREF_ID, response?.id ?: -1)
        sharedPref.setPrefString(PREF_EMAIL, response?.email ?: "")
        sharedPref.setPrefString(PREF_USER_NAME, response?.username ?: "")
        sharedPref.setPrefString(PREF_FIRST_NAME, response?.firstName ?: "")
        sharedPref.setPrefString(PREF_LAST_NAME, response?.lastName ?: "")
        sharedPref.setPrefString(PREF_GENDER, response?.gender ?: "")
        sharedPref.setPrefString(PREF_IMAGE, response?.image ?: "")
        sharedPref.setPrefString(PREF_TOKEN, response?.token ?: "")
        sharedPref.setPrefBoolean(PREF_IS_LOGIN, true)
    }

    fun apiCall(email: String, password: String) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["username"] = email
        map["password"] = password

        repository.login(map).collect { values ->
            _response.value = values
        }
    }

    /* region text watcher */
    fun emailTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            emailValidation(p0.toString())
        }
    }

    fun passwordTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            passwordValidation(p0.toString())
        }
    }

    /* endregion */

    /* region validation */
    fun validation(email: String, password: String): Boolean {
        return emailValidation(email) && passwordValidation(password)
    }

    fun emailValidation(email: String): Boolean {
        val emailValid = validation.emailValidation(email.toString())
        emailError.postValue(emailValid)
        return emailValid.isEmpty()
    }

    fun passwordValidation(password: String): Boolean {
        val passwordValid = validation.passwordValidation(password.toString())
        passwordError.postValue(passwordValid)
        return passwordValid.isEmpty()
    }
    /* endregion */

    fun endIconColorChange(mContext: Context, binding: FragmentLoginBinding) {
        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) ContextCompat.getColor(
                mContext,
                R.color.colorOrange
            ) else ContextCompat.getColor(mContext, R.color.grey_text_color)
            binding.tilEmail.setEndIconTintList(ColorStateList.valueOf(color))
        }
        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) ContextCompat.getColor(
                mContext,
                R.color.colorOrange
            ) else ContextCompat.getColor(mContext, R.color.grey_text_color)
            binding.tilPassword.setEndIconTintList(ColorStateList.valueOf(color))
        }
    }
}