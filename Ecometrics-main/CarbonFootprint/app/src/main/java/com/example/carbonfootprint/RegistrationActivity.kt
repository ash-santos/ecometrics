package com.example.carbonfootprint

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.databinding.RegistrationViewBinding


class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: RegistrationViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.txtSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignUp.setOnClickListener {
            val firstName = binding.txtFirstName.text.toString()
            val lastName = binding.txtLastName.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val confirmPassword = binding.txtConfirmPassword.text.toString()


            val isNotValid = !validateFields(
                binding.txtFirstName,
                binding.txtLastName,
                binding.txtEmail,
                binding.txtPassword,
                binding.txtConfirmPassword
            )

            if (isNotValid) {
                //Fields are incomplete
                return@setOnClickListener
            }

            if (isEmailNotValid(email = email)) {
                Toast.makeText(
                    binding.txtEmail.context,
                    "Invalid email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (isPasswordNotValid(password = password, txtPassword = binding.txtPassword)) {
                return@setOnClickListener
            }

            if (!confirmedPassword(password = password, confirmPassword = confirmPassword)) {
                Toast.makeText(
                    binding.txtPassword.context,
                    "Password is not Match",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val userData = User(
                firstName,
                lastName,
                email,
                password,
                "Active"
            )

            if (FirebaseQuery().createEmailUser(userData = userData)) {
                Toast.makeText(this, "Creating Account Unsuccessful", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Creating Account Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.ckbPrivacyPolicy.setOnClickListener{
            val isChecked = binding.ckbPrivacyPolicy.isChecked

            if (isChecked) {
                showPrivacyTermsDialog(binding.ckbPrivacyPolicy, binding.btnSignUp)
            } else{
                binding.btnSignUp.visibility = View.GONE
            }
        }
    }

    fun showPrivacyTermsDialog( viewCkb : CheckBox, btnSignUp: Button) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.privacy_terms_dialog, null)

        val builder = AlertDialog.Builder(this)
            .setTitle("Privacy Terms")
            .setView(dialogView)
            .setPositiveButton("Accept") { dialog, _ ->
                viewCkb.setChecked(true)
                btnSignUp.visibility = View.VISIBLE
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                viewCkb.setChecked(false)
                btnSignUp.visibility = View.GONE
                dialog.dismiss()
            }
            .setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    private fun validateFields(vararg textViews: TextView): Boolean {
        for (textView in textViews) {
            val text = textView.text.toString().trim()

            if (text.isEmpty()) {
                // If any field is empty, show an error message and return false
                Toast.makeText(
                    textView.context,
                    "${textView.hint} cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }

        // All fields are non-empty, return true
        return true
    }

    private fun confirmedPassword(password: String, confirmPassword: String) =
        password.contentEquals(confirmPassword)

    private fun isEmailNotValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return !emailRegex.matches(email)
    }

    private fun isPasswordNotValid(password: String, txtPassword: TextView): Boolean {
        // Check if the password has a minimum length of 8 characters
        if (password.length < 8) {
            Toast.makeText(
                txtPassword.context,
                "Password must be at least 8 characters long.",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }

        // Check if the password contains at least one lowercase letter
        if (!password.any { it.isLowerCase() }) {
            Toast.makeText(
                txtPassword.context,
                "Password must contain at least one lowercase letter.",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }

        // Check if the password contains at least one uppercase letter
        if (!password.any { it.isUpperCase() }) {
            Toast.makeText(
                txtPassword.context,
                "Password must contain at least one uppercase letter.",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }

        // Check if the password contains at least one digit
        if (!password.any { it.isDigit() }) {
            Toast.makeText(
                txtPassword.context,
                "Password must contain at least one digit.",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }

        // Check if the password contains at least one special character from the specified set
        val specialCharacters = setOf('@', '$', '!', '%', '*', '?', '&', '_')
        if (!password.any { it in specialCharacters }) {
            Toast.makeText(
                txtPassword.context,
                "Password must contain at least one special character.",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }

        return false
    }

    data class User(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val status: String
    )
}