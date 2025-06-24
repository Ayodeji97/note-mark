package com.danzucker.notemark.core.data

import com.danzucker.notemark.core.data.model.AuthInformationSerializable
import com.danzucker.notemark.core.domain.model.AuthInformation

fun AuthInformation.toAuthInformationSerializable(): AuthInformationSerializable {
    return AuthInformationSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken,
        username = username
    )
}

fun AuthInformationSerializable.toAuthInformation(): AuthInformation {
    return AuthInformation(
        accessToken = accessToken,
        refreshToken = refreshToken,
        username = username
    )
}