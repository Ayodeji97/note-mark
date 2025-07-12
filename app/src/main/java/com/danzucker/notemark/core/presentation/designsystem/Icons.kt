package com.danzucker.notemark.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.danzucker.notemark.R

val EyeClosedIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.eye_closed)

val EyeOpenedIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.eye_open)

val LogoutIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.logout)

val EditIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.edit)

val ReadIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.read)