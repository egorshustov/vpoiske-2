package com.egorshustov.vpoiske.feature.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.core.common.model.data
import com.egorshustov.vpoiske.core.domain.token.GetAccessTokenUseCase
import com.egorshustov.vpoiske.core.domain.token.SaveAccessTokenUseCase
import com.egorshustov.vpoiske.core.domain.token.SaveAccessTokenUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    getAccessTokenUseCase: GetAccessTokenUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase
) : ViewModel() {

    private val _state: MutableState<AuthState> = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    init {
        getAccessTokenUseCase(Unit).onEach {
            val accessToken = it.data
            _state.value = state.value.copy(needToFinishAuth = !accessToken.isNullOrBlank())
        }.launchIn(viewModelScope)
    }

    fun onTriggerEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnUpdateLogin -> onUpdateLogin(event.login)
            is AuthEvent.OnUpdatePassword -> onUpdatePassword(event.password)
            AuthEvent.OnStartAuthProcess -> onStartAuthProcess()
            is AuthEvent.OnAuthDataObtained -> onAuthDataObtained(
                event.userId,
                event.accessToken
            )
            AuthEvent.OnNeedToFinishAuthProcessed -> onNeedToFinishAuthProcessed()
            AuthEvent.OnAuthError -> onAuthError()
        }
    }

    private fun onUpdateLogin(login: String) {
        _state.value = state.value.copy(typedLoginText = login)
    }

    private fun onUpdatePassword(password: String) {
        _state.value = state.value.copy(typedPasswordText = password)
    }

    private fun onStartAuthProcess() {
        _state.value = state.value.copy(isLoading = true)
    }

    private fun onAuthDataObtained(userId: String, accessToken: String) {
        viewModelScope.launch {
            saveAccessTokenUseCase(SaveAccessTokenUseCaseParams(accessToken = accessToken))
        }
    }

    private fun onNeedToFinishAuthProcessed() {
        _state.value = state.value.copy(needToFinishAuth = false)
    }

    private fun onAuthError() {
        _state.value = state.value.copy(isLoading = false)
    }
}