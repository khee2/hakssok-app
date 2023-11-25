package com.android.hakssok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.android.hakssok.databinding.ActivityCollegeSelectBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CollegeSelectActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    lateinit var binding: ActivityCollegeSelectBinding
    private var select_item = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollegeSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var textInputLayout = binding.textInputLayout
        var autoCompleteTextView = binding.collegeText

        val college = resources.getStringArray(R.array.college)

        val collegeAdapter = ArrayAdapter(
            this,
            R.layout.item_list, college
        )
        autoCompleteTextView.setAdapter(collegeAdapter)

        autoCompleteTextView.setOnItemClickListener { adapterView, view, position, id ->
            select_item = adapterView.getItemAtPosition(position) as String
        }
        binding.collegeButtonSelect.setOnClickListener {
            val user = db.collection("user")

            db.collection("user").document(LoginApp.id!!)
                .update(
                    mapOf(
                        "college" to select_item
                    ),
                )
                .addOnSuccessListener {
                    val myIntent = Intent(
                        this@CollegeSelectActivity,
                        MainActivity::class.java
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
}