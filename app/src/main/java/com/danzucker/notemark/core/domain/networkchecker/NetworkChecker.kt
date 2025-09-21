package com.danzucker.notemark.core.domain.networkchecker

import kotlinx.coroutines.flow.Flow

interface NetworkChecker {
    fun isDeviceConnected(): Flow<Boolean>
    fun isCurrentlyConnected(): Boolean
}