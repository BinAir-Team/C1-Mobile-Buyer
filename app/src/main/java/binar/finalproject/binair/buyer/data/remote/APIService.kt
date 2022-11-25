package binar.finalproject.binair.buyer.data.remote

import binar.finalproject.binair.buyer.data.response.TicketItem
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("ticket")
    fun getAllTicket() : Call<List<TicketItem>>
}