package com.example.foodie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.btnEdit
import kotlinx.android.synthetic.main.activity_profile.btnHistory
import kotlinx.android.synthetic.main.activity_profile.btnLogOut
import kotlinx.android.synthetic.main.activity_profile.btnSave
import kotlinx.android.synthetic.main.activity_profile.etAddress
import kotlinx.android.synthetic.main.activity_profile.etEmail
import kotlinx.android.synthetic.main.activity_profile.etName
import kotlinx.android.synthetic.main.activity_profile.etPhone
import kotlinx.android.synthetic.main.activity_profile.tvtitle

class ProfileActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var userId = 1
    lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setListener()

    }

    private fun setListener() {
        val bundle = intent.extras

        if (bundle?.getString("email") != null) {
            tvtitle.text = bundle?.getString("email")
        }
        binding.etName.visibility = View.GONE
        binding.etPhone.visibility = View.GONE
        binding.etAddress.visibility = View.GONE
        binding.etEmail.visibility = View.GONE
        binding.btnSave.visibility = View.GONE

        binding.btnSave.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            val address = etAddress.text.toString()
            val email = etEmail.text.toString()
            database = FirebaseDatabase.getInstance().getReference("User")
            val newRef = database.push()
            val user = User(name, phone, email, address)
            newRef.setValue(user)

            //database.child("User").child
            database.child("User/$userId").setValue(user).addOnSuccessListener {
                userId++
                binding.etName.text?.clear()
                binding.etPhone.text?.clear()
                binding.etAddress.text?.clear()
                binding.etEmail.text?.clear()
                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()

                //   tvName.text = "Name"+ name
                //  tvPhone.text = "Phone" +phone
                //  tvAddress.text = "Address" +address
            }.addOnFailureListener {
                Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnHistory.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnEdit.setOnClickListener {
            binding.tvtitle.visibility = View.GONE
            binding.etName.visibility = View.VISIBLE
            binding.etPhone.visibility = View.VISIBLE
            binding.etEmail.visibility = View.VISIBLE
            binding.etAddress.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.GONE
        }
        binding.btnLogOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }
}