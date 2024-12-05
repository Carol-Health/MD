package com.example.carol.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.carol.R
import com.example.carol.utils.ProfilePreferences
import com.example.carol.view.welcome.WelcomeActivity

class ProfileFragment : Fragment() {

    private lateinit var fullNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var contactNumberField: EditText
    private lateinit var saveButton: Button
    private lateinit var editButton: Button
    private lateinit var logoutButton: Button
    private lateinit var profilePreferences: ProfilePreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        fullNameField = view.findViewById(R.id.fullNameField)
        emailField = view.findViewById(R.id.emailField)
        contactNumberField = view.findViewById(R.id.contactNumberField)
        saveButton = view.findViewById(R.id.saveButton)
        editButton = view.findViewById(R.id.editButton)
        logoutButton = view.findViewById(R.id.logoutButton)

        profilePreferences = ProfilePreferences(requireContext())

        loadProfileData()
        setFieldsEditable(false)
        setIconsTint(R.color.grey)

        editButton.setOnClickListener {
            setFieldsEditable(true)
            setIconsTint(R.color.black)
        }

        saveButton.setOnClickListener {
            saveProfileData()
            setFieldsEditable(false)
            setIconsTint(R.color.grey)
        }

        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun setFieldsEditable(editable: Boolean) {
        fullNameField.isEnabled = editable
        emailField.isEnabled = editable
        contactNumberField.isEnabled = editable
        saveButton.isEnabled = editable
        editButton.isEnabled = !editable
    }

    private fun setIconsTint(colorResId: Int) {
        val colorStateList = ContextCompat.getColorStateList(requireContext(), colorResId)
        fullNameField.compoundDrawableTintList = colorStateList
        emailField.compoundDrawableTintList = colorStateList
        contactNumberField.compoundDrawableTintList = colorStateList
    }

    private fun saveProfileData() {
        val fullName = fullNameField.text.toString()
        val email = emailField.text.toString()
        val contactNumber = contactNumberField.text.toString()

        profilePreferences.saveProfile(fullName, email, contactNumber)
        println("Saved Profile Data: Name=$fullName, Email=$email, Contact=$contactNumber")
    }

    private fun loadProfileData() {
        fullNameField.setText(profilePreferences.getName())
        emailField.setText(profilePreferences.getEmail())
        contactNumberField.setText(profilePreferences.getContact())
    }

    private fun logout() {
        profilePreferences.clearProfile()

        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
