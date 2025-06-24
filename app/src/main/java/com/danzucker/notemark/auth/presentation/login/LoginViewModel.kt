package com.danzucker.notemark.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.auth.domain.AuthRepository
import com.danzucker.notemark.auth.domain.UserDataValidator
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val noteAuthRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LoginState())

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeLogin()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailTextChange -> onEmailTextChange(action.text)
            is LoginAction.OnPasswordTextChange -> onPasswordTextChange(action.text)
            LoginAction.OnLoginClick -> login()
            LoginAction.OnRegisterTextClick -> onRegisterTextClick()
            LoginAction.OnTogglePasswordVisibility -> onToggleVisibility()
        }
    }


    private fun observeLogin() {
        combine(email, password) { email, password ->
            _state.update {
                it.copy(
                    email = email.trim(),
                    password = password,
                    canLogin = userDataValidator.isValidEmail(email.trim()) &&
                            userDataValidator.validatePassword(password = password).isValidPassword,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingIn = true) }
            val result = noteAuthRepository.login(
                email = _state.value.email.trim(),
                password = _state.value.password
            )
            _state.update { it.copy(isLoggingIn = false) }

            when (result) {
                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
                is Result.Error -> {
                    eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                }
            }
        }
    }

    private fun onEmailTextChange(text: String) {
        _state.update {
            it.copy(
                email = text,
            )
        }
        email.update { text }
    }

    private fun onPasswordTextChange(text: String) {
        _state.update {
            it.copy(
                password = text
            )
        }
        password.update { text }
    }

    private fun onRegisterTextClick() = viewModelScope.launch {
        eventChannel.send(LoginEvent.OnRegisterTextClick)
    }

    private fun onToggleVisibility() {
        _state.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }
}