package com.example.infystore.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import com.example.infystore.MyApplication
import com.example.infystore.R
import com.example.infystore.databinding.ActivityLoginBinding
import com.example.infystore.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import com.example.infystore.utils.PreferenceHelper.set


/**
 * The LoginActivity , to login user in the application
 * Hilt provide dependency to other classes which have Android entry point
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    /**
     * The binding instance, A LoginBinding
     */
    private lateinit var binding: ActivityLoginBinding
    /**
     * The emailObs, an observer email field
     */
    var emailObs: ObservableField<String> = ObservableField()
    /**
     * The passwordObs, an observer email field
     */
    var passwordObs: ObservableField<String> = ObservableField()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObservable()
    }

    /**
     * The setUpObservable method, to observer editable fields
     */
    private fun setUpObservable() {
        emailObs.set(binding.etEmail.text.toString())
        passwordObs.set(binding.etPassword.text.toString())
        textChangeListener()
    }

    /**
     * The doOnLogin method, to do operation on login
     */
    fun doOnLogin(view: View) {
        if (isValidated()) {
            MyApplication.prefHelper!![Constants.IS_LOGGED_IN] = true
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    /**
     * The isValidated method, to validate fields
     */
    private fun isValidated(): Boolean {
        if (TextUtils.isEmpty(emailObs.get().toString().trim())) {
            binding.tilEmail.error = resources.getString(R.string.required_email_error)
            return false
        } else if (!EMAIL_ADDRESS.matcher(emailObs.get().toString()).matches()) {
            binding.tilEmail.error = resources.getString(R.string.required_email_valid_error)
            return false
        } else if (TextUtils.isEmpty(passwordObs.get().toString().trim())) {
            binding.tilPassword.error = resources.getString(R.string.required_password_error)
            return false
        }
        return true
    }

    /**
     * The textChangeListener method, set fields on text change
     */
    private fun textChangeListener() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                emailObs.set(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilEmail.error = null
            }

        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                passwordObs.set(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilPassword.error = null
            }
        })
    }


}
