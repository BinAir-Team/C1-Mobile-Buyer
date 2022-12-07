package binar.finalproject.binair.buyer.data.remote

import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @POST("register")
    fun registerUser(@Body data : DataRegister): Call<RegisterUserResponse>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email : String, @Field("password") password : String) : Call<LoginResponse>

    @GET("search")
    fun getAllCity() : Call<CityAirportResponse>

    @GET("tickets")
    fun getAllTicket() : Call<AllTicketsResponse>

    @GET("tickets")
    fun getTicketBySearch(@Query("from") cityFrom : String, @Query("airport_from") airportFrom : String , @Query("to") cityTo : String, @Query("airport_to") airportTo : String, @Query("date") date : String, @Query("type") type : String, @Query("willFly") willFly : Boolean) : Call<AllTicketsResponse>

    @POST("trans")
    @Headers("Content-Type: application/json")
    fun bookTicket(@Header("Authorization") token : String, @Body dataTrans : PostBookingBody) : Call<AllTicketsResponse>
}