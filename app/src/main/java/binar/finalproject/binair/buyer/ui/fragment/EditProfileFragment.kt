package binar.finalproject.binair.buyer.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    lateinit var userVM : UserViewModel
    private lateinit var oldPassword : String
    private var image_uri : Uri? = null
    private lateinit var image : MultipartBody.Part
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
    private val REQUEST_CODE_PERMISSION = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    } else {
                        binding.radioPerempuan.isChecked = true
                    }
                    Glide.with(requireContext())
                        .load(it.data.profileImage)
                        .into(binding.imgProfile)
                }
            }
        }else{
            Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
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
            val plainPass = binding.etPass.text.toString()
            val rePass = binding.etKonfirmasiPass.text.toString()

            if(isEqualPassRepassword(plainPass,rePass)){
                val pass = plainPass.toRequestBody("text/plain".toMediaType())
                userVM.updateUser("Bearer $token",firstName,lastName,gender,phone,image, pass).observe(viewLifecycleOwner) {
                    if(it != null){
                        Toast.makeText(requireContext(), "Update Success", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Update Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(requireContext(), "Konfirmasi Password tidak sama", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isEqualPassRepassword(pass: String, repass: String): Boolean {
        return pass == repass
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

    private fun saveImageProfile(applicationContext: Context, bitmap: Bitmap): Uri {
        val name = "img-profile.png"
        val outputDir = File(applicationContext.filesDir, "profiles")
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } finally {
            out?.let {
                try {
                    it.close()
                } catch (ignore: IOException) {
                }

            }
        }
        Log.d("URI_IMG", Uri.fromFile(outputFile).toString())
        return Uri.fromFile(outputFile)
    }
}