package com.android.hakssok

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginApp : MultiDexApplication(){
    companion object {
        lateinit var auth: FirebaseAuth
        var id: String ? = null
        var token_id: String ? = null
        var username: String ? = null
        var college: String ? = null
        var birth: String ? = null
        var profileImage: String ? = null
    }
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}