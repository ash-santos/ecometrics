package com.example.carbonfootprint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.databinding.SignInViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: SignInViewBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser




        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //Auto login
        if (user != null) {
            binding.layoutLoading.visibility = View.VISIBLE
            FirebaseQuery().checkUserProfile(user) { profile ->
                FirebaseQuery().checkInitialEmission(user) { emission ->
                    if (profile.exist && emission.exist) {
                        startActivity(Intent(this, MainActivity::class.java))
                        //startActivity(Intent(this, SignInFirstTime::class.java))
                    } else if (!profile.exist){
                        startActivity(Intent(this, Profiling::class.java))
                    } else {
                        startActivity(Intent(this, SignInFirstTime::class.java))
                    }
                    finish()
                }
            }
        } else {
            Firebase.auth.signOut()
            googleSignInClient.signOut()
        }


        binding.btnSignIn.setOnClickListener {
            userLogin()
            //startActivity(Intent(this, SignInFirstTime::class.java))
        }
        binding.btnNewAccount.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        binding.btnLoginGoogle?.setOnClickListener {
            signInWithGoogle()
        }
        binding.txtForgotPassword.setOnClickListener {
            binding.layoutForgotPassword.visibility = View.VISIBLE
            binding.layoutSignIn.visibility = View.GONE
        }
        binding.txtReturnSignIn.setOnClickListener {
            binding.layoutForgotPassword.visibility = View.GONE
            binding.layoutSignIn.visibility = View.VISIBLE
        }
        binding.btnSubmit.setOnClickListener {
            val email = binding.txtEmailForgotPass.text.toString()
            if (validateEmail(email)) {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Please check your email", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        binding.b1.setOnClickListener{
            binding.txtEmail.setText("lijeg98376@evimzo.com")
            binding.txtPassword.setText("Qwertyuiop@123")
        }
        binding.b2.setOnClickListener{
            binding.txtEmail.setText("baxinoj434@ekposta.com")
            binding.txtPassword.setText("Qwertyuiop@123")
        }
        binding.b3.setOnClickListener{
            binding.txtEmail.setText("pejogop144@dacgu.com")
            binding.txtPassword.setText("Qwertyuiop@123")
        }
        binding.b4.setOnClickListener{
            binding.txtEmail.setText("palek84396@dacgu.com")
            binding.txtPassword.setText("Qwertyuiop@123")
        }
        binding.b5.setOnClickListener{
            binding.txtEmail.setText("jewon17875@felibg.com")
            binding.txtPassword.setText("Qwertyuiop@123")
        }
    }



    private fun userLogin() {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        if (validateForm(email, password)) {
            binding.layoutLoading.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val verification = auth.currentUser?.isEmailVerified
                        if (verification == true) {
                            val user = auth.currentUser
                            user?.let {
                                userType(it)
                            }
                        } else {
                            Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT)
                                .show()
                            binding.layoutLoading.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(this, "Sign In Failed, Please try again", Toast.LENGTH_SHORT)
                            .show()
                        binding.layoutLoading.visibility = View.GONE
                    }
                }
        }
    }

    private fun updateUserDatabase(user: FirebaseUser, firstName: String, lastName: String) {
        val userId = user.uid
        val email = user.email

        //Write user data to Realtime database
        val database = Firebase.database
        val usersRef = database.getReference("Users")

        //Create a new user entry using unique user id
        val userRef = usersRef.child(userId)

        //Set user data
        userRef.child("firstName").setValue(firstName)
        userRef.child("lastName").setValue(lastName)
        userRef.child("email").setValue(email)
    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, "Sign In Failed, Please try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    updateUserDatabase(
                        it,
                        account.givenName.toString(),
                        account.familyName.toString()
                    )
                    userType(it)
                }
            } else {
                Toast.makeText(this, "Sign In Failed, Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun userType(user: FirebaseUser) {
        val userId = user.uid
        database = FirebaseDatabase.getInstance().getReference("UserProfile")
        database.child(userId).get().addOnSuccessListener {
            FirebaseQuery().checkUserProfile(user) { profile ->
                FirebaseQuery().checkInitialEmission(user) { emission ->
                    if (profile.exist && emission.exist) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else if (!profile.exist){
                        startActivity(Intent(this, Profiling::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, SignInFirstTime::class.java))
                        finish()
                    }
                }
            }
        }
    }


    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.txtEmail.error = "Enter valid email address"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding.txtPassword.error = "Enter password"
                binding.txtEmail.error = null
                false
            }

            else -> {
                binding.txtEmail.error = null
                binding.txtPassword.error = null
                true
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmailForgotPass.error = "Enter a valid email address"
            false
        } else {
            true
        }
    }
}