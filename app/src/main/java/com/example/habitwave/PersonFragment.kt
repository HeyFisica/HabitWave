
package com.example.habitwave

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.habitwave.databinding.FragmentPersonBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PersonFragment : Fragment() {

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val PICK_IMAGE_REQUEST = 71
    private val PROFILE_IMAGE_FILE_NAME = "profile_image.png"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentPersonBinding.inflate(inflater, container, false)
        //For Title
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        }



        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        return _binding?.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileImage.setOnClickListener {
            openGallery()
        }

        loadImageFromStorage()
        fetchData()

        // Save button click listener
        binding.updateProfileBtn.setOnClickListener {
            updateProfile()
        }
        // Logout button click listener
        binding.logOutBtn.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        // Delete button click listener
        binding.deleteAccountBtn.setOnClickListener {
            deleteUserAccount()
        }
    }

    private fun fetchData() {
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (isAdded && _binding != null) {
                        if (document != null) {
                            val firstName = document.getString("first_name") ?: ""
                            val lastName = document.getString("last_name") ?: ""
                            val userName = document.getString("user_name") ?: ""
                            val email = document.getString("email") ?: ""
                            val password = document.getString("password") ?: ""

                            binding.First.setText(firstName)
                            binding.lastName.setText(lastName)
                            binding.userName.setText(userName.ifEmpty { "Default not provided" })
                            binding.userEmail.setText(email)
                            val fullName = "$firstName $lastName"
                            binding.fullName.text = fullName
                            binding.userPassword.setText(password)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("PersonFragment", "Error fetching user data: ${e.message}")
                    if (isAdded && _binding != null) {
                        Toast.makeText(
                            activity,
                            "Failed to fetch user data. Retrying...",
                            Toast.LENGTH_SHORT
                        ).show()
                        retryFetchData()
                    }
                }
        }
    }

    private fun retryFetchData() {
        // Simple retry mechanism with a delay
        val handler = Handler()
        handler.postDelayed({
            if (isAdded && _binding != null) {
                fetchData()
            }
        }, 5000) // Retry after 5 seconds
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteUserAccount() {
        val user = mAuth.currentUser
        if (user != null) {
            val credential =
                EmailAuthProvider.getCredential(user.email!!, binding.userPassword.text.toString())

            user.reauthenticate(credential).addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val userId = user.uid

                    // First delete the user data from Firestore
                    db.collection("users").document(userId).delete()
                        .addOnSuccessListener {
                            // If FireStore deletion is successful, delete the Firebase Auth user
                            user.delete().addOnCompleteListener { deleteTask ->
                                if (deleteTask.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Account deleted successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(context, SignUpActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to delete account. ${deleteTask.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Failed to delete user data. ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Re-authentication failed. ${authTask.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateProfile() {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid
            val firstName = binding.First.text.toString()
            val lastName = binding.lastName.text.toString()
            val newEmail = binding.userEmail.text.toString()
            val newPassword = binding.newPassword.text.toString() // New field for new password
            val currentPassword =
                binding.userPassword.text.toString() // New field for current password

            if (currentPassword.isEmpty()) {
                Toast.makeText(context, "Current password cannot be empty.", Toast.LENGTH_LONG)
                    .show()
                return
            }

            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential).addOnCompleteListener { returnTask ->
                if (returnTask.isSuccessful) {
                    db.collection("users").document(userId).get().addOnSuccessListener { document ->
                        if (document != null) {
                            val currentUserName = document.getString("user_name") ?: ""
                            val userName = if (binding.userName.text.toString()
                                    .isNotEmpty()
                            ) binding.userName.text.toString() else currentUserName

                            // Update Firestore user data
                            val userMap = hashMapOf(
                                "first_name" to firstName,
                                "last_name" to lastName,
                                "user_name" to userName,
                                "email" to newEmail,
                                "password" to newPassword
                            )

                            db.collection("users").document(userId).set(userMap)
                                .addOnSuccessListener {
                                    if (newEmail != user.email) {
                                        user.updateEmail(newEmail)
                                            .addOnCompleteListener { emailUpdateTask ->
                                                if (emailUpdateTask.isSuccessful) {
                                                    updatePassword(user, newPassword)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to update email. ${emailUpdateTask.exception?.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        updatePassword(user, newPassword)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        "Failed to update profile. ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Re-authentication failed. ${returnTask.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updatePassword(user: FirebaseUser?, newPassword: String) {
        if (newPassword.isNotEmpty()) {
            user?.updatePassword(newPassword)
                ?.addOnCompleteListener { passwordUpdateTask ->
                    if (passwordUpdateTask.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Profile updated successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to update password. ${passwordUpdateTask.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                binding.profileImage.setImageBitmap(bitmap)
                saveImageToInternalStorage(bitmap)
            } catch (e: IOException) {
                Log.e("PersonFragment", "Error loading image", e)

                e.printStackTrace()
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) {
        val file = File(requireContext().filesDir, PROFILE_IMAGE_FILE_NAME)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: IOException) {
            Log.e("PersonFragment", "Error saving image", e)

            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                Log.e("PersonFragment", "Error closing FileOutputStream", e)

                e.printStackTrace()
            }
        }
    }

    private fun loadImageFromStorage() {
        val file = File(requireContext().filesDir, PROFILE_IMAGE_FILE_NAME)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            binding.profileImage.setImageBitmap(bitmap)
        } else {
            Log.d("PersonFragment", "Profile image file does not exist")
        }
    }
}