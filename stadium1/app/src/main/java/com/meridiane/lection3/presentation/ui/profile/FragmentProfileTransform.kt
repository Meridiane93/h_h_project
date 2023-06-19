package com.meridiane.lection3.presentation.ui.profile

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.meridiane.lection3.ApiState
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentProfileTransformBinding
import com.meridiane.lection3.domain.entity.profile.ChangeUserProfileDomain
import com.meridiane.lection3.presentation.ui.profile.BottomDialogItemFragment.Companion.BUNDLE_KEY_ITEM
import com.meridiane.lection3.presentation.ui.profile.BottomDialogItemFragment.Companion.REQUEST_KEY_ITEM
import com.meridiane.lection3.presentation.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@AndroidEntryPoint
class FragmentProfileTransform : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileTransformBinding
    private var launcherStartCamera: ActivityResultLauncher<Intent>? = null
    private var launcherGetPhoto: ActivityResultLauncher<Intent>? = null
    private var avatarId = "6473541cd8479f1934180b9b"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileTransformBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btTransformProfile.setText(getString(R.string.transform_text_bt))


        binding.imageAddPhoto.setOnClickListener {
            showDialogPhoto(resources.getStringArray(R.array.photo))
        }

        binding.textWorkProfile.setOnClickListener {
            showDialogWork(resources.getStringArray(R.array.work))
        }

        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btTransformProfile.setOnClickListener {
            binding.btTransformProfile.isLoading = true

            viewModel.changeProfile(
                changeProfile(
                    binding.textNameProfile.text.toString(),
                    binding.textSurnameProfile.text.toString(),

                    if (binding.textWorkSwitchProfile.text?.isEmpty() == true)
                        binding.textWorkProfile.text.toString()
                    else binding.textWorkSwitchProfile.text.toString()
                )
            )

        }

        getProfile()
        viewModel.getProfile()

        launcherStartCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as Bitmap

                    val uri = getImageUri(requireContext(), imageBitmap)

                    binding.imagePhoto.load(imageBitmap)

                    viewModel.setPhoto(createFile(uri))
                }
            }


        launcherGetPhoto =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK && result.data != null) {

                    val imageUri = result.data?.data!!

                    binding.imagePhoto.load(imageUri)

                    viewModel.setPhoto(createFile(imageUri))
                }
            }

        lifecycleScope.launch {
            viewModel.changeProfileState.collect{
                if(it != "") findNavController().popBackStack()
            }
        }


        lifecycleScope.launch {
            viewModel.photoState.collect { photo ->
                if (photo.bitmap != null) {
                    binding.imagePhoto.load(photo.bitmap)
                }

            }
        }
    }

    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            name = cursor.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }

    private fun getProfile() {
        lifecycleScope.launch {

            viewModel.profileState.collectLatest { state ->
                when (state) {
                    is ApiState.Loading -> binding.btTransformProfile.isLoading = true

                    is ApiState.Success -> {
                        with(binding) {
                            textNameProfile.setText(state.data.name)
                            textSurnameProfile.setText(state.data.surname)
                            textWorkProfile.setText(state.data.occupation)
                            avatarId = state.data.avatarUrl!!
                            viewModel.getPhoto(state.data.avatarUrl!!)

                            if (state.data.avatarUrl!!.all { it.isDigit() }) imagePhoto.load(R.drawable.ic_photo)
                            else {
                                imagePhoto.load(state.data.avatarUrl) {
                                    crossfade(true)
                                    crossfade(100)
                                    placeholder(R.drawable.ic_photo)
                                    transformations(CircleCropTransformation())
                                }

                            }
                        }
                        binding.btTransformProfile.setText(getString(R.string.transform_text_bt))
                    }

                    is ApiState.Error -> {
                        Log.d("MyLog", "ApiState Error")
                    }
                }
            }
        }
    }

    private fun showDialogPhoto(listSize: Array<String>) {
        childFragmentManager.setFragmentResultListener(REQUEST_KEY_ITEM, this) { _, bundle ->

            val result = bundle.getString(BUNDLE_KEY_ITEM)

            if (result == getString(R.string.add_photo)) {
                val takePicker = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                try {
                    launcherStartCamera?.launch(takePicker)
                } catch (e: ActivityNotFoundException) {
                    Log.d("MyLog", "launcherStartCamera $e")
                }
            } else {
                val getImage = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                try {
                    launcherGetPhoto?.launch(getImage)
                } catch (e: ActivityNotFoundException) {
                    Log.d("MyLog", "launcherGetPhoto $e")
                }
            }


        }

        val modalBottomSheet = BottomDialogItemFragment(listSize)

        modalBottomSheet.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        modalBottomSheet.show(childFragmentManager, BottomDialogItemFragment.TAG)
    }


    private fun showDialogWork(listSize: Array<String>) {
        childFragmentManager.setFragmentResultListener("request_key_item", this)
        { _, bundle ->
            val result = bundle.getString("bundleKey_item")

            binding.textWorkProfile.setText(result)
            if (result == getString(R.string.work_another))
                binding.layoutWorkSwitchProfile.visibility = View.VISIBLE
            else binding.layoutWorkSwitchProfile.visibility = View.INVISIBLE

        }
        val modalBottomSheet = BottomDialogItemFragment(listSize)

        modalBottomSheet.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        modalBottomSheet.show(childFragmentManager, BottomDialogItemFragment.TAG)
    }


    private fun createFile(uri: Uri): MultipartBody.Part {
        val parcelFileDescriptor =
            requireContext().contentResolver.openFileDescriptor(uri, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)

        val file = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(uri)
        )

        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val requestFile = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            file
        )
        val body =
            MultipartBody.Part.createFormData("uploadedFile", file.name, requestFile)

        parcelFileDescriptor.close()

        return body
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun changeProfile(
        name: String,
        surname: String,
        occupation: String
    ): List<ChangeUserProfileDomain> = listOf(
        ChangeUserProfileDomain(path = "/name", value = name),
        ChangeUserProfileDomain(path = "/surname", value = surname),
        ChangeUserProfileDomain(path = "/occupation", value = occupation)
    )

}