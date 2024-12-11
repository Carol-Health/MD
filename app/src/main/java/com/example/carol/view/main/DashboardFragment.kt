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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchUserName()
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
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        ApiClient.apiService.getHistory(uid).enqueue(object : Callback<HistoryApiResponse> {
            override fun onResponse(
                call: Call<HistoryApiResponse>,
                response: Response<HistoryApiResponse>
            ) {
                if (response.isSuccessful) {
                    val historyList = response.body()?.data ?: emptyList()
                    historyAdapter.submitList(historyList)
                } else {
                    Toast.makeText(context, "Failed to fetch history", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<HistoryApiResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserName() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Mengambil referensi Firestore
        val firestore = FirebaseFirestore.getInstance()

        // Mengambil dokumen pengguna berdasarkan uid
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val displayName = document.getString("username") ?: "Nama tidak tersedia"

                    // Menampilkan nama pengguna di TextView
                    binding.userNameTextView.text = "Hello, $displayName!"
                } else {
                    binding.userNameTextView.text = "Nama tidak ditemukan"
                }
            }
            .addOnFailureListener {
                binding.userNameTextView.text = "Error memuat nama"
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}