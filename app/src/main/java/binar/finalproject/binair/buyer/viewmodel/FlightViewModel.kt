package binar.finalproject.binair.buyer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.data.remote.APIService
import binar.finalproject.binair.buyer.data.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private val client : APIService) : ViewModel() {
    private val _allTicket = MutableLiveData<List<TicketItem>?>()
    val allTicket : LiveData<List<TicketItem>?> = _allTicket

    fun callGetAllTicket() {
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
    }
}

class PromoViewModel @Inject constructor(private val client : APIService) : ViewModel() {
    private val _allPromo = MutableLiveData<List<DataPromo>?>()
    val allPromo: LiveData<List<DataPromo>?> = _allPromo

    fun callgetPromo(){
        client.getPromo().enqueue(object : retrofit2.Callback<AllPromoResponse> {
            override fun onResponse(
                call: Call<AllPromoResponse>,
                response: Response<AllPromoResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _allPromo.postValue(result.data)
                        Log.d("Result", "result: $result")
                    } else {
                        _allPromo.postValue(null)
                    }
                } else {
                    Log.e("error", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllPromoResponse>, t: Throwable) {
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })

    }
}




