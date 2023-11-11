package com.android.hakssok

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.hakssok.LoginApp
import com.android.hakssok.R
import com.android.hakssok.databinding.LoginAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding: LoginAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LoginAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(LoginApp.checkAuth()){
            changeVisibility("login")
        }else {
            changeVisibility("logout")
        }

        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            //구글 로그인 거쳐서 나온 화면 처리
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!! // 인증서 받아오기
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                LoginApp.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful){
                            Log.d("노경희","여기여기 성공성공3")
                            LoginApp.email = account.email // 인증했던 이메일 저장
                            changeVisibility("login")
                        }else {
                            changeVisibility("logout")
                        }
                    }
            }catch (e: ApiException){
                changeVisibility("logout")
            }
        }

        binding.logoutBtn.setOnClickListener{
            // 로그아웃
            LoginApp.auth.signOut()
            LoginApp.email = null
            changeVisibility("logout")
        }

        binding.googleLoginBtn.setOnClickListener {
            //구글 인증 처리하기
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            // 구글 인증 앱 실행
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent // 인텐트 객체 생성
            requestLauncher.launch(signInIntent) // 인텐트 시작
        }

    }

    fun changeVisibility(mode: String) {
        if (mode === "login") {
            binding.run {
                authMainTextView.text = "${LoginApp.email}님 반갑습니다."
                logoutBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.GONE
            }

        } else if (mode === "logout") {
            binding.run {
                logoutBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.VISIBLE
            }
        } else if (mode === "signin") {
            binding.run {
                logoutBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
            }
        }
    }
}