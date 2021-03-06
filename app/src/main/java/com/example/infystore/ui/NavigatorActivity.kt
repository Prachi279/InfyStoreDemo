package com.example.infystore.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.infystore.utils.Constants
import com.example.infystore.utils.PreferenceHelper.get
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

/**
 * The NavigatorActivity , to navigate user based on the condition
 * Hilt provide dependency to other classes which have Android entry point
 */
@AndroidEntryPoint
class NavigatorActivity : AppCompatActivity() {
    /**
     *The applicationPref, SharedPreference Instance
     */
    @Inject
    @Named("Pref")
    lateinit var applicationPref: SharedPreferences

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateUser()
    }

    /**
     * The navigateUser method, to navigate user based on condition
     */
    private fun navigateUser() {
        val intent: Intent = if (applicationPref[Constants.IS_LOGGED_IN, false]!!) {
            Intent(this@NavigatorActivity, MainActivity::class.java)
        } else {
            Intent(
                this@NavigatorActivity,
                LoginActivity::class.java
            )
        }
        startActivity(intent)
        finish()
    }
}