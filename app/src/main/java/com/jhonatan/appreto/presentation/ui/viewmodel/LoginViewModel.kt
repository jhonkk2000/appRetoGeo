package com.jhonatan.appreto.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonatan.appreto.data.repository.LoginRepository
import com.jhonatan.appreto.data.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository): ViewModel() {

    private val _loginStateFlow: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Init)
    val loginState: StateFlow<LoginState> = _loginStateFlow

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginStateFlow.value = LoginState.Loading
        repository.loginFlow(email, password).collect {
            if (it?.success != null && it.success){
                _loginStateFlow.value = LoginState.SuccesLogin
            }else {
                _loginStateFlow.value = LoginState.FailLogin(it?.message ?: "Ocurrió un error al intentar iniciar sesión.")
            }
        }
    }

}