package com.android.hakssok

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginApp : MultiDexApplication(){
    companion object {
        lateinit var auth: FirebaseAuth
        var id: String ? = null
        var username: String ? = null
    }
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}