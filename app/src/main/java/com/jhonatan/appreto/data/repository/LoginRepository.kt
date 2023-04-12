package com.jhonatan.appreto.data.repository

import com.jhonatan.appreto.data.models.LoginResponse
import com.jhonatan.appreto.data.models.ResponseBase
import com.jhonatan.appreto.data.network.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(private val service: LoginService) {

    fun loginFlow(email: String, password: String): Flow<ResponseBase?> = flow {
        emit(service.login(email, password))
    }.flowOn(Dispatchers.IO)

}