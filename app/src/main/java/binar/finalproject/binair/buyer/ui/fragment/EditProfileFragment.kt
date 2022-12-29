@file:Suppress("BlockingMethodInNonBlockingContext", "BlockingMethodInNonBlockingContext",
    "BlockingMethodInNonBlockingContext", "BlockingMethodInNonBlockingContext"
)

package binar.finalproject.binair.buyer.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.databinding.FragmentEditProfileBinding
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userVM : UserViewModel
    private var image : MultipartBody.Part? = null
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.imgProfile.setImageURI(result)
            val contentResolver = activity?.contentResolver
            val type = contentResolver?.getType(result!!)

            val tempFile = File.createTempFile("image", ".jpg",null)
            val inputStream = contentResolver?.openInputStream(result!!)
            tempFile.outputStream().use {
                inputStream?.copyTo(it)
            }
            val reqBody : RequestBody = tempFile.asRequestBody(type!!.toMediaType())
            image = MultipartBody.Part.createFormData("profile_image", tempFile.name, reqBody)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        getDataUser()
    }

    private fun setListener(){
        binding.apply {
            imgProfile.setOnClickListener {
                checkingPermissions()
            }
            imgEditProfile.setOnClickListener {
                checkingPermissions()
            }
            btnSimpan.setOnClickListener {
                updateUser()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDataUser(){
        val prefs = requireActivity().getSharedPreferences(Constant.dataUser, 0)
        val token = prefs.getString("token", null)
        if(token != null){
            userVM.getUser("Bearer $token").observe(viewLifecycleOwner) {
                if(it != null) {
                    val data = it.data
                    binding.tvName.setText(data.firstname + " " + data.lastname)
                    binding.etNamaDepan.setText(data.firstname)
                    binding.etNamaBelakang.setText(data.lastname)
                    binding.etEmail.setText(data.email)
                    binding.etPhone.setText(data.phone)
                    if(data.gender == "Laki-laki"){
                        binding.radioLaki.isChecked = true
                    }else if(data.gender == "Perempuan") {
                        binding.radioPerempuan.isChecked = true
                    }
                    Glide.with(requireContext())
                        .load(it.data.profileImage)
                        .into(binding.imgProfile)
                }
            }
        }
    }

    private fun updateUser(){
        val prefs = requireActivity().getSharedPreferences(Constant.dataUser, 0)
        val token = prefs.getString("token", null)
        if(token != null) {
            val firstName = binding.etNamaDepan.text.toString().toRequestBody("text/plain".toMediaType())
            val lastName = binding.etNamaBelakang.text.toString().toRequestBody("text/plain".toMediaType())
            val gender = if(binding.radioLaki.isChecked){
                "Laki-laki".toRequestBody("text/plain".toMediaType())
            } else {
                "Perempuan".toRequestBody("text/plain".toMediaType())
            }
            val phone = binding.etPhone.text.toString().toRequestBody("text/plain".toMediaType())

            if(image != null){
                userVM.updateUser("Bearer $token",firstName,lastName,gender,phone,image!!).observe(viewLifecycleOwner) {
                    if(it != null){
                        Toast.makeText(requireContext(), "Update Berhasil", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Update Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                userVM.updateUserWithoutImage("Bearer $token",firstName,lastName,gender,phone).observe(viewLifecycleOwner) {
                    if(it != null){
                        Toast.makeText(requireContext(), "Update Berhasil", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Update Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }else{
            Toast.makeText(requireContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,)
        ){
            openGallery()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun validateInput(): Boolean {
        var isValid = true
        binding.apply {
            if (etNamaDepan.text.toString().isEmpty()) {
                etNamaDepan.error = "Nama Depan tidak boleh kosong"
                isValid = false
            }
            if (etNamaBelakang.text.toString().isEmpty()) {
                etNamaBelakang.error = "Nama Belakang tidak boleh kosong"
                isValid = false
            }
            if (etEmail.text.toString().isEmpty()) {
                etEmail.error = "Email tidak boleh kosong"
                isValid = false
            }
            if (etPhone.text.toString().isEmpty()) {
                etPhone.error = "Phone tidak boleh kosong"
                isValid = false
            }
        }
        return isValid
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION = 100
    }
}