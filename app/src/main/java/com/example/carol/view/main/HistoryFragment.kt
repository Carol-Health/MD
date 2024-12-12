package com.example.carol.view.main

import HistoryAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carol.R
import com.example.carol.databinding.FragmentHistoryBinding
import com.example.carol.network.ApiClient
import com.example.carol.network.HistoryApiResponse
import com.example.carol.network.HistoryResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        Log.d("HistoryFragment", "onCreateView: Binding initialized")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter { selectedHistory ->
            navigateToDetail(selectedHistory)
        }
        binding.recycleView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = historyAdapter
        }
    }

    private fun navigateToDetail(history: HistoryResponse) {
        val bundle = Bundle().apply {
            putString("diseaseName", history.diseaseName)
            putString("date", history.date)
            putString("imageUrl", history.imageUrl)
            putString("description", history.description)
            putString("treatment", history.treatment)
        }

        val detailFragment = HistoryDetailFragment()
        detailFragment.arguments = bundle

        detailFragment.show(parentFragmentManager, "HistoryDetailFragment")
    }

    private fun fetchHistory() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        binding.progressBar.visibility = View.VISIBLE

        ApiClient.apiService.getHistory(uid).enqueue(object : Callback<HistoryApiResponse> {
            override fun onResponse(
                call: Call<HistoryApiResponse>,
                response: Response<HistoryApiResponse>
            ) {
                if (!isAdded || _binding == null) return

                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val historyList = response.body()?.data ?: emptyList()
                    val sortedHistoryList = historyList.sortedByDescending { it.date }
                    historyAdapter.submitList(sortedHistoryList)
                } else {
                    Toast.makeText(context, "Failed to fetch history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HistoryApiResponse>, t: Throwable) {
                if (!isAdded || _binding == null) return

                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("HistoryFragment", "onDestroyView: Clearing binding")
        _binding = null
    }


}