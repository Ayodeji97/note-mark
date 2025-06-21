package com.danzucker.notemark.auth.data.validator

import android.util.Patterns
import com.danzucker.notemark.auth.domain.PatternValidator

class EmailPatternValidator : PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}