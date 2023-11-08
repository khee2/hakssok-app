package com.android.hakssok

import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.android.hakssok.databinding.RestaurantBinding


class RestaurantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = RestaurantBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val categorySpinner: Spinner = findViewById(R.id.category_spinner)
        val restaurantList: Array<String> = arrayOf("양식", "한식", "일식", "중식", "카페/주류")

        val adapter = SpinnerAdapter(this, restaurantList)
        categorySpinner.adapter = adapter
    }
}