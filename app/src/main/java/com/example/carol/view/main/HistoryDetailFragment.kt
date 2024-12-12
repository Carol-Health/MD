package com.example.carol.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.carol.R
import com.example.carol.databinding.FragmentHistoryDetailBinding


class HistoryDetailFragment : DialogFragment() {
    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari argument
        val diseaseName = arguments?.getString("diseaseName")
        val date = arguments?.getString("date")
        val imageUrl = arguments?.getString("imageUrl")
        val description = arguments?.getString("description")
        val treatment = arguments?.getString("treatment")

        val correctedUrl = imageUrl?.replace(
            "carol-image-predict/images/carol-image-predict/images",
            "carol-image-predict/images"
        )
        // Tampilkan data ke UI
        binding.diseaseNameTextView.text = diseaseName
        binding.dateTextView.text = date
        binding.descriptionTextView.text = description
        binding.treatmentTextView.text = treatment

        Glide.with(this)
            .load(correctedUrl)
            .into(binding.diseaseImageView)

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}