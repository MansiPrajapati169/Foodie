package com.example.foodie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    // lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            "kotlinsharedpreference",
            Context.MODE_PRIVATE
        )

        intent = if (sharedPreferences.getBoolean("isLogin", false)) {
            Intent(
                this, ProfileActivity::class.java
            )
        } else {
            Intent(
                this,
                LogInActivity::class.java
            )
        }

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_up_to_bottom_splash)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_bottom_to_up_splash)

        binding.logoSplash.animation = topAnimation
        binding.motoSplash.animation = bottomAnimation

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 3000)
        supportActionBar?.hide()

    }

    private fun setHandler() {


//        auth = FirebaseAuth.getInstance()
//        val currentuser = auth.currentUser
//        if(currentuser == null) {
//
//        }
    }
}