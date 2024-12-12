package com.example.carol.view.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.carol.R
import com.example.carol.network.ApiClient
import com.example.carol.network.DetectionResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class DeteksiFragment : Fragment() {

    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null
    private var selectedImageFile: File? = null
    private val PICK_IMAGE_REQUEST = 1

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.extras?.get("data") as Bitmap?
                photo?.let {
                    imageView.setImageBitmap(it)
                    selectedImageFile = saveBitmapToFile(it)
                    selectedImageUri = Uri.fromFile(selectedImageFile)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_deteksi, container, false)

        imageView = view.findViewById(R.id.imageView)
        val chooseImageButton: Button = view.findViewById(R.id.chooseImageButton)
        val useCameraButton: Button = view.findViewById(R.id.useCameraButton)
        val analyzeButton: Button = view.findViewById(R.id.analyzeButton)

        chooseImageButton.setOnClickListener { openGallery() }
        useCameraButton.setOnClickListener { openCamera() }
        analyzeButton.setOnClickListener { analyzeImage() }

        savedInstanceState?.getString("selectedImageUri")?.let {
            selectedImageUri = Uri.parse(it)
            imageView.setImageURI(selectedImageUri)
            selectedImageFile = selectedImageUri?.let { uriToFile(it) }
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Simpan Uri gambar
        outState.putString("selectedImageUri", selectedImageUri?.toString())
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                selectedImageUri = it
                imageView.setImageURI(it)
                selectedImageFile = uriToFile(it)
            }
        }
    }

    private fun analyzeImage() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        if (selectedImageFile == null) {
            Toast.makeText(context, "Please select an image first!", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), selectedImageFile!!)
        val body = MultipartBody.Part.createFormData("file", selectedImageFile!!.name, requestFile)
        val uidBody = RequestBody.create("text/plain".toMediaTypeOrNull(), uid)

        ApiClient.apiService.detectDisease(body, uidBody).enqueue(object : Callback<DetectionResponse> {
            override fun onResponse(
                call: Call<DetectionResponse>,
                response: Response<DetectionResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    navigateToResultActivity(result)
                } else {
                    Toast.makeText(context, "Failed to analyze image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
                Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun navigateToResultActivity(result: DetectionResponse?) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("diseaseName", result?.name ?: "Unknown")
            putExtra("description", result?.description ?: "No description available.")
            putExtra("treatment", result?.treatment ?: "No treatment available")
            putExtra("imageUri", selectedImageUri.toString())
        }
        startActivity(intent)
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload_", ".jpg", requireContext().cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        inputStream?.close()
        return tempFile
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val file = File.createTempFile("camera_image", ".jpg", requireContext().cacheDir)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return file
    }
}