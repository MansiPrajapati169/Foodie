package com.example.foodie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie.databinding.ActivityShowProfileBinding
import com.google.firebase.database.DatabaseReference

class ShowProfile : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityShowProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityShowProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        show()
    }
    private fun show() {
         val bundle = intent.extras
         if(bundle?.getString("name") != null)
         {
             binding.tvName.text = "Welcome" + bundle?.getString("name")
             binding.tvPhone.text = bundle?.getString("phone")
             binding.tvAddress.text = bundle?.getString("Address")
         }
    }
}