package com.example.foodie

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_log_in.etEmailAddress
import kotlinx.android.synthetic.main.activity_log_in.etPassword

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var tvRedirectSignUp: TextView
    lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    lateinit var btnLogin: Button
    private lateinit var sharedPref: SharedPreferences

    // Creating firebaseAuth object
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // View Binding

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()
        setListener()
        val currentuser = auth.currentUser
        if (currentuser != null) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)

        }
    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            // using finish() to end the activity
            finish()
        }
    }

    private fun login() {
        val email = binding.etEmailAddress.text.toString()
        val pass = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, " Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            binding.etEmailAddress.error = "Email cant be blank"
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            //Toast.makeText(this, " Emailnot valid", Toast.LENGTH_SHORT).show()
            binding.etEmailAddress.error = "Email Address Invalid"
            return
        }
        if (pass.isEmpty()) {
            binding.etPassword.error = "Password cant blank"
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val firebaseUser: FirebaseUser? = auth.currentUser

                val userId: String? = firebaseUser?.uid
                val userEmail: String? = firebaseUser?.email
                sharedPref = getPreferences(MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("firebasekey", userId)
                editor.commit()

                //other way tried for session
                /*  if(isLogin == "1"){
                           var email=intent.getStringExtra("email")
                           if(email!= null) {
                               setText(email)
                               with(sharedPref.edit()){
                                   putString("email",email)
                                   apply()
                               }
                           }
                       }
                       else {
                           val intent = Intent(this, MainActivity::class.java)
                           startActivity(intent)
                           finish()
                       }*/
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)

            } else
                Toast.makeText(this, "Log In failed Please Signup", Toast.LENGTH_SHORT).show()
        }
    }

}
