package binar.finalproject.binair.buyer.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.remote.APIService
import binar.finalproject.binair.buyer.data.response.AllTicketsResponse
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.data.response.CityAirportResponse
import binar.finalproject.binair.buyer.data.response.TicketItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FlightRepository @Inject constructor(var client: APIService) {
    private val _allTicket = MutableLiveData<List<TicketItem>?>()
    val allTicket : LiveData<List<TicketItem>?> = _allTicket
    private val _allCityAirport = MutableLiveData<List<CityAirport>?>()
    val allCityAirport : LiveData<List<CityAirport>?> = _allCityAirport

    fun callGetAllTicket() : LiveData<List<TicketItem>?> {
        client.getAllTicket().enqueue(object : retrofit2.Callback<AllTicketsResponse> {
            override fun onResponse(
                call: Call<AllTicketsResponse>,
                response: Response<AllTicketsResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _allTicket.postValue(result.data)
                        Log.d("RESULT", "Result : $result")
                    }else{
                        _allTicket.postValue(null)
                    }
                }else{
                    Log.e("Error : ", "onFailed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllTicketsResponse>, t: Throwable) {
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
        return allTicket
    }

    fun callGetCityAirport() : LiveData<List<CityAirport>?> {
        client.getAllCity().enqueue(object : Callback<CityAirportResponse>{
            override fun onResponse(
                call: Call<CityAirportResponse>,
                response: Response<CityAirportResponse>
            ) {
                if (response.isSuccessful){
                    val result = response.body()
                    if (result != null){
                        _allCityAirport.postValue(result.data)
                        Log.d("RESULT", "Result City : $result")
                    }else{
                        _allCityAirport.postValue(null)
                    }
                }else{
                    Log.e("Error : ", "onFailed: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CityAirportResponse>, t: Throwable) {
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
        return allCityAirport
    }
}