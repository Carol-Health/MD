package com.example.carol.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.carol.R
import com.example.carol.view.login.LoginActivity
import com.example.carol.view.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private lateinit var displayNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            return null
        }

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        displayNameEditText = view.findViewById(R.id.displayNameTextView)
        emailEditText = view.findViewById(R.id.emailField)
        logoutButton = view.findViewById(R.id.logoutButton)

        loadUserProfile(user)

        logoutButton.setOnClickListener {
            performLogout()
        }

        return view
    }

    private fun loadUserProfile(user: FirebaseUser) {
        val displayName = user.displayName ?: "Nama tidak tersedia"
        val email = user.email ?: "Email tidak tersedia"

        android.util.Log.d("ProfileFragment", "Display Name: $displayName")

        displayNameEditText.setText(displayName)
        emailEditText.setText(email)
    }


    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
