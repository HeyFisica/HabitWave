package com.example.habitwave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.habitwave.databinding.ActivityLoginBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "Login"
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mAuth.setLanguageCode("en")
        db = FirebaseFirestore.getInstance()
        // Set the desired locale here

        // Check if the user is already logged in
        val currentUser = mAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            goToHomeActivity()
        }


        binding.signUpABtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginABtn.setOnClickListener {
            val loginEmail = binding.edtEmailLogin.text.toString().trim()
            val loginPassword = binding.edtPasswordLogin.text.toString().trim()

            if (TextUtils.isEmpty(loginEmail)) {
                binding.edtEmailLogin.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(loginPassword)) {
                binding.edtPasswordLogin.error = "Password is required"
                return@setOnClickListener
            }

            checkCurrentUser(loginEmail, loginPassword)
        }
    }

    private fun checkCurrentUser(email: String, password: String) {
        Log.d("LoginActivity", "Attempting to log in user with email: $email")
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "Login successful, attempting to reload user.")
                    val user: FirebaseUser? = mAuth.currentUser
                    user?.reload()?.addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful) {
                            if (user != null) {
                                Log.d("LoginActivity", "User reload successful. Email verified: ${user.isEmailVerified}")
                                if (user.isEmailVerified) {
                                    Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show()
                                    goToHomeActivity()
                                } else {
                                    Toast.makeText(this, "Please verify your email address.", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Log.e("LoginActivity", "User reload successful but user is null.")
                                Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("LoginActivity", "Failed to reload user: ${reloadTask.exception?.message}")
                            Toast.makeText(this, "Failed to reload user.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("LoginActivity", "Login failed: ${task.exception?.message}")
                    Toast.makeText(this, "Login failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, ChooserActivity::class.java)
        startActivity(intent)
        finish()
    }



}



