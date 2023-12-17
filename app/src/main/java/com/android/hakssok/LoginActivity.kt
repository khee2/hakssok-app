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
                val credential = GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                ) // TODO account.id를 사용하게 되면 TAG 에러가 발생하게 됨. 이유 더 찾아 보기
                LoginApp.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            LoginApp.id = account.id // 시간이 지나 갱신되는 idToken 대신 값이 항상 값이 일정한 id를 사용. 리뷰 등록 및 내 리뷰 관리에서 id를 사용하여 데이터를 저장하고 불러옴.
                            LoginApp.token_id = account.idToken
                            LoginApp.username = account.familyName + account.givenName
                            // TODO 더 좋은 방법: intent putExtra로 넘기기 (LoginActivity -> CollegeSelectActivity)
                            db.collection("user").document(LoginApp.id!!).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot.exists()) { // 기존 사용자
                                        LoginApp.college = documentSnapshot.getString("college")
                                        val myIntent = Intent(
                                            this@LoginActivity,
                                            RealActivity::class.java
                                        )
                                        startActivity(myIntent)
                                        finish()
                                    } else { // 신규 사용자 등록
                                        val user_info = hashMapOf(
                                            "name" to account.familyName + account.givenName,
                                            "email" to account.email,
                                            "id" to account.id,
                                            "college" to ""
                                        )

                                        db.collection("user").document(LoginApp.id!!)
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
                                .addOnFailureListener { e ->
                                    Log.e("예외 발생", "Firebase 데이터베이스에서 문서를 가져오는 중 오류 발생", e)
                                }
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.exception)
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