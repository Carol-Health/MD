package com.example.carol.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.carol.R
import com.example.carol.databinding.ActivityMainBinding
import com.example.carol.view.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val SELECTED_FRAGMENT_KEY = "selected_fragment_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        } else {
            val selectedFragmentTag = savedInstanceState.getString(SELECTED_FRAGMENT_KEY)
            val fragment = supportFragmentManager.findFragmentByTag(selectedFragmentTag)
            if (fragment != null) {
                loadFragment(fragment)
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.navigation_dashboard -> DashboardFragment()
                R.id.navigation_detection -> DeteksiFragment()
                R.id.navigation_history -> HistoryFragment()
                R.id.navigation_profile -> ProfileFragment()
                else -> null
            }

            selectedFragment?.let { loadFragment(it) }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        currentFragment?.let {
            outState.putString(SELECTED_FRAGMENT_KEY, it::class.java.simpleName)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
