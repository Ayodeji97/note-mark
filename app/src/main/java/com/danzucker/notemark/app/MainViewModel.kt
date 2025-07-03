package com.danzucker.notemark.app 
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if(!hasLoadedInitialData) {
                /** Load initial data here **/
                checkUserSession()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    private fun checkUserSession() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isCheckingAuth = true,
                    isAuthCheckComplete = false
                )
            }

            val authInfo = withContext(Dispatchers.IO) {
                sessionStorage.get()
            }

            _state.update {
                it.copy(
                    isLoggedIn = authInfo != null,
                    isCheckingAuth = false,
                    isAuthCheckComplete = true,
                )
            }
        }
    }

}