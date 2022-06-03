package com.example.infystore.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.infystore.MyApplication
import com.example.infystore.R
import com.example.infystore.databinding.ActivityMainBinding
import com.example.infystore.utils.CommonUtils
import com.example.infystore.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import com.example.infystore.utils.PreferenceHelper.set

/**
 * The MainActivity, A Base class of Fragments
 * Hilt provide dependency to other classes which have Android entry point
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**
     * The binding, MainActivity binding instace
     */
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialSetup()
    }

    /**
     * The initialSetup method, setup bottom navigation controller
     */
    private fun initialSetup() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setSupportActionBar(binding.toolbar)
        navView.setupWithNavController(navController)
    }

    /**
     * The doOnLogout method, to show logout dialog and clear preference on logout
     */
    fun doOnLogout(view: View) {
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            setRedirection()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        CommonUtils.showAlertDialog(
            view,
            getString(R.string.logout_message),
            positiveButtonClick,
            negativeButtonClick
        )

    }

    /**
     * The setRedirection method, to redirect user to login screen
     */
    private fun setRedirection() {
        cleanPreferences()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    /**
     * The cleanPreferences method, to clean the preferences on logout
     */
    private fun cleanPreferences() {
        MyApplication.prefHelper!![Constants.IS_LOGGED_IN] = false
        CommonUtils.saveObjIntoPref(null, Constants.ORDER_LIST)
    }

}