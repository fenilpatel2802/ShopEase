package com.wipro.shopease.common

import android.content.Context
import com.wipro.shopease.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val EMAIL_PATTERN =
    """[a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+"""

class Validation @Inject constructor(@ApplicationContext val context: Context) {

    fun emailValidation(email: String): String {
        val error = when {
            email.isEmpty() -> context.getString(R.string.email_address_required)
//            email.notMatches(EMAIL_PATTERN) -> context.getString(R.string.invalid_email_address)
            else -> ""
        }
        return error
    }

    fun passwordValidation(password: String): String {
        val error = when {
            password.isEmpty() -> context.getString(R.string.password_required)
            else -> ""
        }
        return error
    }

}