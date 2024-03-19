package ru.arinae_va.lensa.utils

import android.util.Patterns

fun isValidPhoneNumber(phoneNumber: String) = phoneNumber
    .startsWith(Constants.RUSSIA_COUNTRY_CODE) && phoneNumber.length == Constants.RUSSIAN_PHONE_NUMBER_LENGTH

fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()