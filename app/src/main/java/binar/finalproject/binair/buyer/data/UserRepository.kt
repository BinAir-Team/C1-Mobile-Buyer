package binar.finalproject.binair.buyer.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.remote.APIService
import binar.finalproject.binair.buyer.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(var apiService: APIService) {
    private val _registerUser = MutableLiveData<RegisterUserResponse?>()
    val registerUser : LiveData<RegisterUserResponse?> = _registerUser
    private val _loginUser = MutableLiveData<LoginResponse?>()
    val loginUser : LiveData<LoginResponse?> = _loginUser
    private val _currentUser = MutableLiveData<GetUserResponse?>()
    val currentUser : LiveData<GetUserResponse?> = _currentUser
    private val _updateUser = MutableLiveData<UpdateUserResponse?>()
    val updateUser : LiveData<UpdateUserResponse?> = _updateUser

    fun registerUser(dataUser : DataRegister) : LiveData<RegisterUserResponse?> {
        apiService.registerUser(dataUser).enqueue(object : Callback<RegisterUserResponse>{
            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                if (response.isSuccessful){
                    val dataResponse = response.body()
                    _registerUser.postValue(dataResponse)
//                    Log.d("LogRegister", dataResponse.toString())
                }else{
                    _registerUser.postValue(null)
                    Log.e("Error not successful : ", response.message())
                }
            }

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                _registerUser.postValue(null)
                Log.d("Error onFailure : ", t.message!!)
            }
        })
        return registerUser
    }

    fun loginUser(email : String, password : String) : LiveData<LoginResponse?> {
        apiService.loginUser(email,password).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val dataResponse = response.body()
                if (response.isSuccessful){
                    _loginUser.postValue(dataResponse)
                }else{
                    _loginUser.postValue(response.body())
                    Log.d("Error not successful : ", response.body().toString())
                    Log.e("Error not successful : ", response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                _loginUser.postValue(null)
                Log.d("Error onFailure : ", t.message!!)
            }
        })
        return loginUser
    }

    fun getUser(token : String) : LiveData<GetUserResponse?> {
        apiService.getUser(token).enqueue(object : Callback<GetUserResponse>{
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful){
                    val dataResponse = response.body()
                    _currentUser.postValue(dataResponse)
                }else{
                    _currentUser.postValue(null)
                    Log.e("Error not successful : ", response.message())
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                _currentUser.postValue(null)
                Log.d("Error onFailure : ", t.message!!)
            }
        })
        return currentUser
    }

    fun updateUser(token : String,firstName : RequestBody, lastName : RequestBody, gender : RequestBody, phone : RequestBody, profileImage : MultipartBody.Part) : LiveData<UpdateUserResponse?> {
        apiService.updateUser(token,firstName,lastName,gender,phone,profileImage).enqueue(object : Callback<UpdateUserResponse>{
            override fun onResponse(
                call: Call<UpdateUserResponse>,
                response: Response<UpdateUserResponse>
            ) {
                if (response.isSuccessful){
                    val dataResponse = response.body()
                    _updateUser.postValue(dataResponse)
                }else{
                    _updateUser.postValue(null)
                    Log.e("Error not successful : ", response.message())
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _updateUser.postValue(null)
                Log.d("Error onFailure : ", t.message!!)
            }
        })
        return updateUser
    }

    fun updateUserWithoutImage(token : String,firstName : RequestBody, lastName : RequestBody, gender : RequestBody, phone : RequestBody) : LiveData<UpdateUserResponse?>{
        apiService.updateUserWithoutImage(token, firstName, lastName, gender, phone).enqueue(object : Callback<UpdateUserResponse>{
            override fun onResponse(
                call: Call<UpdateUserResponse>,
                response: Response<UpdateUserResponse>
            ) {
                if (response.isSuccessful){
                    val dataResponse = response.body()
                    _updateUser.postValue(dataResponse)
                }else{
                    _updateUser.postValue(null)
                    Log.e("Error not successful : ", response.message())
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _updateUser.postValue(null)
                Log.d("Error onFailure : ", t.message!!)
            }
        })
        return updateUser
    }

    fun updatePassword(token : String, oldPass : String, newPass : String, confirmPass : String) : LiveData<UpdateUserPasswordResponse?> {
        val updatePass = MutableLiveData<UpdateUserPasswordResponse?>()
        apiService.updatePassword(token,oldPass,newPass,confirmPass).enqueue(object : Callback<UpdateUserPasswordResponse>{
            override fun onResponse(
                call: Call<UpdateUserPasswordResponse>,
                response: Response<UpdateUserPasswordResponse>
            ) {
                if (response.isSuccessful){
                    val dataResponse = response.body()
                    updatePass.postValue(dataResponse)
                }else{
                    updatePass.postValue(null)
                    Log.e("Error not successful : ", response.message())
                }
            }

            override fun onFailure(call: Call<UpdateUserPasswordResponse>, t: Throwable) {
                updatePass.postValue(null)
                Log.d("Error onFailure : ", t.message!!)
            }
        })
        return updatePass
    }
}