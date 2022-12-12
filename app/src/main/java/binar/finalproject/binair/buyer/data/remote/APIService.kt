package binar.finalproject.binair.buyer.data.remote

import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @POST("register")
    fun registerUser(@Body data : DataRegister): Call<RegisterUserResponse>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email : String, @Field("password") password : String) : Call<LoginResponse>

    @GET("user")
    fun getUser(@Header("Authorization") token : String) : Call<GetUserResponse>

    @Multipart
    @PUT("user")
    fun updateUser(@Header("Authorization") token : String, @Part("firstname") firstName : RequestBody, @Part("lastname") lastName : RequestBody, @Part("gender") gender : RequestBody, @Part("phone") phone : RequestBody, @Part profileImage : MultipartBody.Part, @Part("password") pass : RequestBody) : Call<UpdateUserResponse>

    @GET("search")
    fun getAllCity() : Call<CityAirportResponse>

    @GET("tickets")
    fun getAllTicket() : Call<AllTicketsResponse>

    @GET("tickets")
    fun getTicketBySearch(@Query("from") cityFrom : String, @Query("airport_from") airportFrom : String , @Query("to") cityTo : String, @Query("airport_to") airportTo : String, @Query("date") date : String, @Query("type") type : String, @Query("willFly") willFly : Boolean) : Call<AllTicketsResponse>

    @POST("trans")
    fun bookTicket(@Header("Authorization") token : String, @Body dataTrans : PostBookingBody) : Call<BookingTicketResponse>

    @Multipart
    @PUT("trans/{id}")
    fun updatePaymet(@Header("Authorization") token : String, @Path("id") idTransaction : RequestBody, @Part paymentImage : MultipartBody.Part) : Call<UpdatePaymentResponse>
}