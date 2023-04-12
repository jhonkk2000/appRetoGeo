package com.jhonatan.appreto.data.network

import com.jhonatan.appreto.data.models.ResponseBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class LoginService @Inject constructor(private val api: ApiGeo) {

    suspend fun login(email: String, password: String): ResponseBase? {
        return withContext(Dispatchers.IO) {
            var responseBase: ResponseBase? = null
            try {
                val params = mutableMapOf<String, Any>()
                params["email"] = email
                params["password"] = password
                responseBase = api.login(params)
            }catch (e: HttpException){
                if (e.code() == 401){
                    responseBase = ResponseBase(message = "Identidad o contraseña no válida.", success = false)
                }
            }
            responseBase
        }
    }

}