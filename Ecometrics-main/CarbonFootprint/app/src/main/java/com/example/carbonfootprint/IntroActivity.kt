package com.example.carbonfootprint

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.Manifest
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.carbonfootprint.databinding.IntroActivityViewBinding


class IntroActivity : AppCompatActivity() {


    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 100
    }

    private lateinit var binding: IntroActivityViewBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize SharedPreferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        // Check if the activity has been opened before
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (!isFirstRun) {
            // Open the activity for the first time
            // Update the flag to indicate that the activity has been opened
            with(sharedPreferences.edit()) {
                putBoolean("isFirstRun", false)
                apply()

                checkPermission(Manifest.permission.POST_NOTIFICATIONS,
                    NOTIFICATION_PERMISSION_CODE)
            }

        }else {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        fadeInAnimation(binding.layoutIntro1)


        binding.btnIntro1.setOnClickListener(){
            binding.layoutIntro1.visibility = View.GONE
            binding.layoutIntro2.visibility = View.VISIBLE
            fadeInAnimation(binding.layoutIntro2)
        }
        binding.btnIntro2.setOnClickListener(){
            binding.layoutIntro2.visibility = View.GONE
            binding.layoutIntro3.visibility = View.VISIBLE
            fadeInAnimation(binding.layoutIntro3)
        }
        binding.btnIntro3.setOnClickListener(){
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
    private fun fadeInAnimation(view: View) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 2000 // milliseconds
        view.startAnimation(fadeIn)

        // Set the visibility of the view to VISIBLE after the animation
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }
    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}