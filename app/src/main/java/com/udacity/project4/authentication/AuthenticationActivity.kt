package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
@Suppress("DEPRECATION")
class AuthenticationActivity : AppCompatActivity() {

    private val TAG = "AuthenticationActivity"

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1001
    }

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        binding.loginBtn.setOnClickListener { launchSignInFlow() }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), SIGN_IN_REQUEST_CODE
        )
    }

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == SIGN_IN_REQUEST_CODE) {
        val response = IdpResponse.fromResultIntent(data)
        if (resultCode == Activity.RESULT_OK) {
            // User successfully signed in
            //go to Reminders activity
            goToReminder()
            Log.i(
                TAG,
                "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
            )
        } else {
            Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
        }
    }
}

    private fun goToReminder(){
        startActivity(Intent(this, RemindersActivity::class.java))
        finish()
    }
}
