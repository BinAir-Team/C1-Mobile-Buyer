package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.data.UserRepository
import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.response.GetUserResponse
import binar.finalproject.binair.buyer.data.response.LoginResponse
import binar.finalproject.binair.buyer.data.response.RegisterUserResponse
import binar.finalproject.binair.buyer.data.response.UpdateUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private var userRepo : UserRepository) : ViewModel() {
    fun registerUser(dataUser : DataRegister) : LiveData<RegisterUserResponse?> = userRepo.registerUser(dataUser)
    fun loginUser(email : String, password : String) : LiveData<LoginResponse?> = userRepo.loginUser(email, password)
    fun getUser(token : String) : LiveData<GetUserResponse?> = userRepo.getUser(token)
    fun updateUser(token : String, firstName : RequestBody, lastName : RequestBody, gender : RequestBody, phone : RequestBody, password : RequestBody, profileImage : MultipartBody.Part): LiveData<UpdateUserResponse?> = userRepo.updateUser(token, firstName,lastName,gender, phone, password, profileImage)
}