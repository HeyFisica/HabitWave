package com.example.habitwave

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.habitwave.databinding.ActivitySignUpBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "Sign Up"
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()






        binding.signUpBtn.setOnClickListener {
            val firstName = binding.edtfirstName.text.toString().trim()
            val lastName = binding.edtlastName.text.toString().trim()
            val userName = binding.userName.text.toString().trim()
            val signUpEmail = binding.edtSignUPEmail.text.toString().trim()
            val signUpPassword = binding.edtSignupPassword.text.toString().trim()

            if (TextUtils.isEmpty(firstName)) {
                binding.edtfirstName.error = "First Name is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(lastName)) {
                binding.edtlastName.error = "Last Name is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(userName)) {
                binding.userName.error = "User Name is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(signUpEmail)) {
                binding.edtSignUPEmail.error = "Email is required"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(signUpPassword)) {
                binding.edtSignupPassword.error = "Password is required"
                return@setOnClickListener
            }

            registerUser(signUpEmail, signUpPassword, firstName, lastName, userName)
        }

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        userName: String
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val userId = user?.uid
                    if (user != null) {
                        val userData = hashMapOf(
                            "first_name" to firstName,
                            "last_name" to lastName,
                            "user_name" to userName,
                            "email" to email,
                            "password" to password
                        )
                        db.collection("users").document(userId!!)
                            .set(userData)
                            .addOnSuccessListener {
                                user.sendEmailVerification().addOnCompleteListener { verifyTask ->
                                    if (verifyTask.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Registration successful. Please check your email for verification.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Failed to send verification email.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Failed to save user data. ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}





