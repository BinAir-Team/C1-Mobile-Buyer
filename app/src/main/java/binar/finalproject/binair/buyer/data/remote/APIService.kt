package binar.finalproject.binair.buyer.data.remote

import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.response.AllTicketsResponse
import binar.finalproject.binair.buyer.data.response.LoginResponse
import binar.finalproject.binair.buyer.data.response.RegisterUserResponse
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @POST("register")
    fun registerUser(@Body data : DataRegister): Call<RegisterUserResponse>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email : String, @Field("password") password : String) : Call<LoginResponse>

    @GET("tickets")
    fun getAllTicket() : Call<AllTicketsResponse>
}