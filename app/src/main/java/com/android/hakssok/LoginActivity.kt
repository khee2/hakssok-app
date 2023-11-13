package com.android.hakssok

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.hakssok.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.firestore


class LoginActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                LoginApp.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            LoginApp.token_id = account.idToken

                            val user = db.collection("user")

                            val user_info = hashMapOf(
                                "name" to account.familyName + account.givenName,
                                "email" to account.email,
                                "idToken" to account.idToken,
                                "college" to ""
                            )

                            db.collection("user").document(account.idToken!!)
                                .set(user_info)
                                .addOnSuccessListener {
                                    val myIntent = Intent(
                                        this@LoginActivity,
                                        CollegeSelectActivity::class.java
                                    )
                                    startActivity(myIntent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(
                                        "실패",
                                        "Error writing document",
                                        e
                                    )
                                }
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.exception)
//                            changeVisibility("logout")
                        }
                    }
            } catch (e: ApiException) {
            }
        }

        binding.googleLoginBtn.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }

    }
}