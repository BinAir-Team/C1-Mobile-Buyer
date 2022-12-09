package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.data.UserRepository
import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.response.GetUserResponse
import binar.finalproject.binair.buyer.data.response.LoginResponse
import binar.finalproject.binair.buyer.data.response.LogoutResponse
import binar.finalproject.binair.buyer.data.response.RegisterUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private var userRepo : UserRepository) : ViewModel() {
    fun registerUser(dataUser : DataRegister) : LiveData<RegisterUserResponse?> = userRepo.registerUser(dataUser)
    fun loginUser(email : String, password : String) : LiveData<LoginResponse?> = userRepo.loginUser(email, password)
    fun getUser(token : String) : LiveData<GetUserResponse?> = userRepo.getUser(token)
    fun logout(token : String) : LiveData<LogoutResponse?> = userRepo.logout(token)
}