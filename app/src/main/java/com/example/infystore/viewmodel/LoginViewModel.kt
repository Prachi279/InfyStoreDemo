package com.example.infystore.viewmodel

import android.text.Editable
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.google.android.material.textfield.TextInputLayout


/**
 * The LoginViewModel class, to setup business logic
 */
class LoginViewModel : ViewModel() {
    /**
     * The emailObs, an observer email field
     */
    var emailObs: ObservableField<String> = ObservableField()

    /**
     * The passwordObs, an observer email field
     */
    var passwordObs: ObservableField<String> = ObservableField()

    /**
     * The callPasswordAfterTextChange method, AfterTextChange listener for password field
     */
    fun callPasswordAfterTextChange(str: Editable) {
        passwordObs.set(str.toString())
    }

    /**
     * The callEmailAfterTextChange method, AfterTextChange listener for email field
     */
    fun callEmailAfterTextChange(str: Editable) {
        emailObs.set(str.toString())
    }

    /**
     * The callPasswordTextChangeLister method, onTextChange listener for password field
     */
    fun callPasswordTextChangeLister(view: View) {
        (view as TextInputLayout).error = null
    }

    /**
     * The callEmailTextChangeLister method, onTextChange listener for email field
     */
    fun callEmailTextChangeLister(view: View) {
        (view as TextInputLayout).error = null
    }
}