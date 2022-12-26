package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.repository.UserRepository
import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private var userRepo : UserRepository) : ViewModel() {
    fun registerUser(dataUser : DataRegister) : LiveData<RegisterUserResponse?> = userRepo.registerUser(dataUser)
    fun loginUser(email : String, password : String) : LiveData<LoginResponse?> = userRepo.loginUser(email, password)
    fun loginGoogle(token : String) : LiveData<LoginGoogleResponse?> = userRepo.loginGoogle(token)
    fun getLoginErrorMessage() : LiveData<String> = userRepo.loginErrorMessage
    fun getUser(token : String) : LiveData<GetUserResponse?> = userRepo.getUser(token)
    fun updateUser(token : String, firstName : RequestBody, lastName : RequestBody, gender : RequestBody, phone : RequestBody, profileImage : MultipartBody.Part): LiveData<UpdateUserResponse?> = userRepo.updateUser(token, firstName,lastName,gender, phone, profileImage)
    fun updateUserWithoutImage(token : String, firstName : RequestBody, lastName : RequestBody, gender : RequestBody, phone : RequestBody) : LiveData<UpdateUserResponse?> = userRepo.updateUserWithoutImage(token, firstName, lastName, gender, phone)
    fun updatePassword(token : String, oldPassword : String, newPassword : String, confirmPass : String) : LiveData<UpdateUserPasswordResponse?> = userRepo.updatePassword(token, oldPassword, newPassword, confirmPass)
}