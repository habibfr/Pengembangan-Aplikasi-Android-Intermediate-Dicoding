package com.habibfr.mystoryapp.view.posting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.habibfr.mystoryapp.R
import com.habibfr.mystoryapp.data.Result
import com.habibfr.mystoryapp.data.remote.retrofit.ApiConfig
import com.habibfr.mystoryapp.databinding.ActivityPostingBinding
import com.habibfr.mystoryapp.databinding.ActivityStoryBinding
import com.habibfr.mystoryapp.view.ViewModelFactory
import com.habibfr.mystoryapp.view.main.MainActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class PostingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingBinding
    private var currentImageUri: Uri? = null
    private val postingViewModel by viewModels<PostingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.descriptionStory.text.toString()
            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            postingViewModel.postStory(multipartBody, requestBody)

            postingViewModel.postStatus.observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                        is Result.Error -> {
                            binding.progressIndicator.visibility = View.GONE
                            Toast.makeText(
                                this, result.error, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }


//            finish()

//            lifecycleScope.launch {
//                try {
//                    val apiService = ApiConfig.getApiService()
//                    val successResponse = apiService.uploadImage(multipartBody, requestBody)
//                    showToast(successResponse.message)
//                    showLoading(false)
//                } catch (e: HttpException) {
//                    val errorBody = e.response()?.errorBody()?.string()
//                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
//                    showToast(errorResponse.message)
//                    showLoading(false)
//                }
//            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}