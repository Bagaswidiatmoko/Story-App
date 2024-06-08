package com.dicoding.picodiploma.loginwithanimation.view.addstory

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityAddStoryBinding
import com.dicoding.picodiploma.loginwithanimation.utils.getImageUri
import com.dicoding.picodiploma.loginwithanimation.utils.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.utils.uriToFile
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private var locationManager: LocationManager? = null

    private var currentImageUri: Uri? = null

    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(this@AddStoryActivity)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        with(binding) {
            buttonGallery.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            buttonAdd.setOnClickListener {
                if (cbLocation.isChecked) {
                    getMyLocation()
                } else {
                    uploadStory()
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                showLoading(true)
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            } catch (ex: SecurityException) {
                Toast.makeText(
                    this,
                    "Security Exception, no location available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onLocationChanged(location: Location) {
            uploadStoryWithLocation(location)
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { launcherIntentCamera.launch(it) }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
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
            showToast(getString(R.string.empty_image_warning))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivStoryImage.setImageURI(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.edAddDescription.text.toString()

            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            addStoryViewModel.addStory(multipartBody, requestBody).observe(this@AddStoryActivity) {
                if (it != null) {
                    when (it) {
                        is Result.Error -> {

                            showLoading(false)
                            showAlert(getString(R.string.gagal), it.error, false)
                        }

                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            showAlert(
                                getString(R.string.berhasil),
                                getString(R.string.story_anda_berhasil_ditambahkan), true
                            )
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadStoryWithLocation(location: Location) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.edAddDescription.text.toString()
            val lat = location.latitude.toString()
            val lon = location.longitude.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val positionLat = lat.toRequestBody("text/plain".toMediaType())
            val positionLon = lon.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )





            addStoryViewModel.addStoryWithLocation(
                multipartBody,
                requestBody,
                positionLat,
                positionLon
            )
                .observe(this@AddStoryActivity) {
                    if (it != null) {
                        when (it) {
                            is Result.Error -> {

                                showLoading(false)
                                showAlert(getString(R.string.gagal), it.error, false)
                            }

                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                showAlert(
                                    getString(R.string.berhasil),
                                    getString(R.string.story_anda_berhasil_ditambahkan), true
                                )
                            }
                        }
                    }
                }
        } ?: showToast(getString(R.string.empty_image_warning))
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlert(title: String, message: String, finish: Boolean) {
        AlertDialog.Builder(this@AddStoryActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                if (finish) {
                    finish()
                }
            }
            create()
            show()
        }
    }
}