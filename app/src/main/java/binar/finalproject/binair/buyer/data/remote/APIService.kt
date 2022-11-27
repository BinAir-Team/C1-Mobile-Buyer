package binar.finalproject.binair.buyer.data.remote

import binar.finalproject.binair.buyer.data.response.AllTicketsResponse
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("tickets")
    fun getAllTicket() : Call<AllTicketsResponse>
}