package com.example.carol.view.main

import HistoryAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carol.databinding.FragmentDashboardBinding
import com.example.carol.network.ApiClient
import com.example.carol.network.HistoryApiResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter

    private var displayName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            displayName = savedInstanceState.getString("username")
        }

        setupRecyclerView()
        if (displayName != null) {
            binding.userNameTextView.text = "Hello, $displayName!"
        } else {
            fetchUserName()
        }
        fetchHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun fetchHistory() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        binding.progressBar.visibility = View.VISIBLE

        ApiClient.apiService.getHistory(uid).enqueue(object : Callback<HistoryApiResponse> {
            override fun onResponse(
                call: Call<HistoryApiResponse>,
                response: Response<HistoryApiResponse>
            ) {
                if (response.isSuccessful) {
                    val historyList = response.body()?.data ?: emptyList()

                    val sortedHistoryList = historyList.sortedByDescending { it.date }

                    binding.progressBar.visibility = View.GONE
                    if (isAdded) {
                        historyAdapter.submitList(sortedHistoryList)
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HistoryApiResponse>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun fetchUserName() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (isAdded && _binding != null) {
                    if (document.exists()) {
                        val name = document.getString("username") ?: "Nama tidak tersedia"
                        displayName = name
                        binding.userNameTextView.text = "Hello, $name!"
                    } else {
                        binding.userNameTextView.text = "Nama tidak ditemukan"
                    }
                }
            }
            .addOnFailureListener {
                if (isAdded && _binding != null) {
                    binding.userNameTextView.text = "Error memuat nama"
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("username", displayName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

