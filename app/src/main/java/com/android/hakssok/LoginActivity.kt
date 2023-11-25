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
        Log.d("야야", "2야야")
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        {
            Log.d("야야", "3야야")
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                Log.d("야야", "4야야")
                val account = task.getResult(ApiException::class.java)!!
                Log.d("야야", "5야야")
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                Log.d("야야", "6야야")
                LoginApp.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            LoginApp.id = account.id
                            db.collection("user").document(account.id!!).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot.exists()) {
                                        val myIntent = Intent(
                                            this@LoginActivity,
                                            MainActivity::class.java
                                        )
                                        startActivity(myIntent)
                                        finish()
                                    } else {
                                        val user_info = hashMapOf(
                                            "name" to account.familyName + account.givenName,
                                            "email" to account.email,
                                            "id" to account.id,
                                            "college" to ""
                                        )

                                        db.collection("user").document(account.id!!)
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
                                    }
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
            Log.d("야야", "1야야")
            requestLauncher.launch(signInIntent)
        }

    }
}