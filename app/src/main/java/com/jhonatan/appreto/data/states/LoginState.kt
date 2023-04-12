package com.jhonatan.appreto.data.states

sealed interface LoginState {
    object Init: LoginState
    object SuccesLogin: LoginState
    object Loading: LoginState
    class FailLogin(val message: String): LoginState
}