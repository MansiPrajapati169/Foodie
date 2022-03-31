package com.example.foodie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.foodie.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
    lateinit var tvRedirectLogin: TextView
    lateinit var db : FirebaseFirestore

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialising auth object
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        setListener()
    }
    private fun setListener() {
        binding.btnSSigned.setOnClickListener {
            signUpUser()
        }
        // switching from signUp Activity to Login Activity
        binding.tvRedirectLogin.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signUpUser() {
       // val name = etName.text.toString()
        val email = binding.etSEmailAddress.text.toString()
        val pass = binding.etSPassword.text.toString()
        val confirmPassword = binding.etSConfPassword.text.toString()
        val Users = db.collection("USERS")
        val user = hashMapOf(
            "email" to email
        )
        val query = Users.whereEqualTo("email",email).get().addOnSuccessListener { it ->
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this) {
                    task->
                    if(task.isSuccessful) {
                        Users.document(email).set(user)
                    }
                    else {
                        Toast.makeText(this, "Authentication fail", Toast.LENGTH_LONG).show()

                    }
                }
            } else {
                Toast.makeText(this, "Already Registered", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)

            }
        }
        // check pass
        if ( email.isEmpty() || pass.isEmpty() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Name, Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
           etEmail.error = "Email not Valid"
            return
        }
        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
       if(!isValidPassword(pass)) {
          etPass.error = "Password Invalid"
            return
        }
        // email and pass in it.

        }

    private fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)
            return passwordMatcher.find(password) != null
        } ?: return false
    }
}
