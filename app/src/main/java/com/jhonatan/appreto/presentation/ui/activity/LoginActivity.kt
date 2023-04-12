package com.jhonatan.appreto.presentation.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.jhonatan.appreto.data.states.LoginState
import com.jhonatan.appreto.databinding.ActivityLoginBinding
import com.jhonatan.appreto.presentation.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnLogin()

        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { state ->
                when (state) {
                    LoginState.Init -> Unit
                    LoginState.Loading -> setLoading()
                    LoginState.SuccesLogin -> successLogin()
                    is LoginState.FailLogin -> failLogin(state.message)
                }
            }
        }
    }

    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Rellena ambos campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(email, password)
        }
    }

    private fun successLogin() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setLoading(){
        binding.progressLogin.visibility = View.VISIBLE
    }

    private fun failLogin(message: String) {
        binding.progressLogin.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}