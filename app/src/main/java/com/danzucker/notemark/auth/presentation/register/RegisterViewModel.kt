package com.danzucker.notemark.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.auth.domain.UserDataValidator
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

class RegisterViewModel(
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())

    private val username = MutableStateFlow("")
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val confirmPassword = MutableStateFlow("")

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeRegister()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnUsernameTextChange -> onUsernameTextChange(action.text)
            is RegisterAction.OnEmailTextChange -> onEmailTextChange(action.text)
            is RegisterAction.OnPasswordTextChange -> onPasswordTextChange(action.text)
            is RegisterAction.OnConfirmPasswordTextChange -> onConfirmPasswordTextChange(action.text)
            RegisterAction.OnRegisterClick -> onRegister()
            RegisterAction.OnLoginTextClick -> onLoginTextClick()
        }
    }

    private fun observeRegister() {
        combine(username, email, password, confirmPassword) { username, email, password, confirmPassword ->
            val usernameValidationState = userDataValidator.validateUsername(username.trim())
            val isEmailValid = userDataValidator.isValidEmail(email.trim())
            val passwordValidationState = userDataValidator.validatePassword(password, confirmPassword)

            _state.update {
                it.copy(
                    username = username.trim(),
                    email = email.trim(),
                    password = password,
                    confirmPassword = confirmPassword,
                    isEmailValid = isEmailValid,
                    passwordValidationState = passwordValidationState,
                    usernameValidationState = usernameValidationState,
                    canRegister = usernameValidationState.hasValidCharacters &&
                            isEmailValid &&
                            passwordValidationState.isValidPassword &&
                            passwordValidationState.hasValidConfirmPassword
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun onRegister() {}

    private fun onLoginTextClick() = viewModelScope.launch {
        eventChannel.send(RegisterEvent.OnLoginTextClick)
    }

    private fun onUsernameTextChange(text: String) {
        _state.update {
            it.copy(
                username = text
            )
        }
        username.update { text }
    }

    private fun onEmailTextChange(text: String) {
        _state.update {
            it.copy(
                email = text
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

    private fun onConfirmPasswordTextChange(text: String) {
        _state.update {
            it.copy(
                confirmPassword = text
            )
        }
        confirmPassword.update { text }
    }

//    fun getSupportingText(field: Field): UiText {
//       return when (field) {
//            USERNAME -> UiText.StringResourceWithArgs(R.string.username_supporting_text)
//            EMAIL -> {
//                UiText.StringResourceWithArgs(R.string.empty_string) // no supporting text
//            }
//            PASSWORD -> {
//                UiText.StringResourceWithArgs(R.string.password_supporting_text)
//            }
//            CONFIRM_PASSWORD -> {
//                UiText.StringResourceWithArgs(R.string.empty_string) // no supporting text
//            }
//        }
//    }
}

enum class Field {
    USERNAME,
    EMAIL,
    PASSWORD,
    CONFIRM_PASSWORD
}