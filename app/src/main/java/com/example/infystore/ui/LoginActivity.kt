package com.example.infystore.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.infystore.R
import com.example.infystore.databinding.ActivityLoginBinding
import com.example.infystore.utils.CommonUtils
import com.example.infystore.utils.Constants
import com.example.infystore.utils.MyForegrounndService
import com.example.infystore.utils.MyWorkManager
import com.example.infystore.utils.PreferenceHelper.set
import com.example.infystore.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


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
     *The applicationPref, SharedPreference Instance
     */
    @Inject
    @Named("Pref")
    lateinit var applicationPref: SharedPreferences

    /**
     * The loginViewModel, An Instance of LoginViewModel
     */
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)
        //binding of viewmodel class with xml file variable viewmodel is
        // compulsory if we want to call any method or perform operations from the xml
        binding.loginViewModel = loginViewModel
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        /*if (!CommonUtils.foregroundServiceRunning(this)) {
            val serviceIntent = Intent(this, MyForegrounndService::class.java)
            startForegroundService(serviceIntent)
        }*/
        /* val downloadRequest: WorkRequest =
             OneTimeWorkRequestBuilder<MyWorkManager>().setConstraints(
                 Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
             ).build()

         WorkManager
             .getInstance(applicationContext)
             .enqueue(downloadRequest)*/
    }

    /**
     * The doOnLogin method, to do operation on login
     */
    fun doOnLogin(view: View) {
        if (isValidated()) {
            applicationPref[Constants.IS_LOGGED_IN] = true
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }


    /**
     * The isValidated method, to validate fields
     */
    private fun isValidated(): Boolean {
        if (TextUtils.isEmpty(loginViewModel.emailObs.get().toString().trim())) {
            binding.tilEmail.error = resources.getString(R.string.required_email_error)
            return false
        } else if (!EMAIL_ADDRESS.matcher(loginViewModel.emailObs.get().toString()).matches()) {
            binding.tilEmail.error = resources.getString(R.string.required_email_valid_error)
            return false
        } else if (loginViewModel.passwordObs.get() == null || TextUtils.isEmpty(
                loginViewModel.passwordObs.get().toString().trim()
            )
        ) {
            binding.tilPassword.error = resources.getString(R.string.required_password_error)
            return false
        }
        return true
    }

}
