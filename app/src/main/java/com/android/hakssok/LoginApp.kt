package com.android.hakssok

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class LoginApp : MultiDexApplication(){
    companion object {
        lateinit var auth: FirebaseAuth
        var email: String ? = null
        fun checkAuth(): Boolean { // 인증된 유저인지 체크
            val currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}