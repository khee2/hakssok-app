package com.android.hakssok

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginApp : MultiDexApplication(){
    companion object {
        lateinit var auth: FirebaseAuth
        var id: String ? = "103454243208198857541"
        var username: String ? = "노경희"
    }
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}