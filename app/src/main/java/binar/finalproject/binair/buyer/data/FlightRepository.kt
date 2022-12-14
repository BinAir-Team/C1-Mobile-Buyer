package binar.finalproject.binair.buyer.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.remote.APIService
import binar.finalproject.binair.buyer.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FlightRepository @Inject constructor(var client: APIService) {
    private val _allTicket = MutableLiveData<List<TicketItem>?>()
    val allTicket: LiveData<List<TicketItem>?> = _allTicket
    private val _allCityAirport = MutableLiveData<List<CityAirport>?>()
    val allCityAirport: LiveData<List<CityAirport>?> = _allCityAirport
    private val _adultPassenger = MutableLiveData<Int>(1)
    val adultPassenger: LiveData<Int> = _adultPassenger
    private val _childPassenger = MutableLiveData<Int>()
    val childPassenger: LiveData<Int> = _childPassenger
    private val _totalPassenger = MutableLiveData<Int>()
    val totalPassenger: LiveData<Int> = _totalPassenger
    private val _chosenTicket = MutableLiveData<TicketItem?>()
    val chosenTicket: MutableLiveData<TicketItem?> = _chosenTicket
    private val _ticketBySearch = MutableLiveData<List<TicketItem>?>()
    val ticketBySearch: LiveData<List<TicketItem>?> = _ticketBySearch
    private val _bookTicket = MutableLiveData<BookingTicketResponse?>()
    val bookTicket: MutableLiveData<BookingTicketResponse?> = _bookTicket
    private val _updatePayment = MutableLiveData<UpdatePaymentResponse?>()
    val updatePayment: MutableLiveData<UpdatePaymentResponse?> = _updatePayment

    fun callGetAllTicket(): LiveData<List<TicketItem>?> {
        client.getAllTicket().enqueue(object : Callback<AllTicketsResponse> {
            override fun onResponse(
                call: Call<AllTicketsResponse>,
                response: Response<AllTicketsResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _allTicket.postValue(result.data)
                        Log.d("RESULT", "Result : $result")
                    } else {
                        _allTicket.postValue(null)
                    }
                } else {
                    Log.e("Error : ", "onFailed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllTicketsResponse>, t: Throwable) {
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
        return allTicket
    }

    fun callGetTicketBySearch(
        from: String,
        airport_from: String,
        to: String,
        airport_to: String,
        date: String,
        type: String,
        willFly: Boolean = true
    ): LiveData<List<TicketItem>?> {
        client.getTicketBySearch(from, airport_from, to, airport_to, date, type)
            .enqueue(object : Callback<AllTicketsResponse> {
                override fun onResponse(
                    call: Call<AllTicketsResponse>,
                    response: Response<AllTicketsResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            _ticketBySearch.postValue(result.data)
                        } else {
                            _ticketBySearch.postValue(null)
                        }
                    } else {
                        _ticketBySearch.postValue(null)
                        Log.e("Error : ", "onFailed: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<AllTicketsResponse>, t: Throwable) {
                    _ticketBySearch.postValue(null)
                    Log.e("Error : ", "onFailure: ${t.message}")
                }
            })
        return ticketBySearch
    }

    fun callGetCityAirport(): LiveData<List<CityAirport>?> {
        client.getAllCity().enqueue(object : Callback<CityAirportResponse> {
            override fun onResponse(
                call: Call<CityAirportResponse>,
                response: Response<CityAirportResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _allCityAirport.postValue(result.data)
                        Log.d("RESULT", "Result City : $result")
                    } else {
                        _allCityAirport.postValue(null)
                    }
                } else {
                    Log.e("Error : ", "onFailed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CityAirportResponse>, t: Throwable) {
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
        return allCityAirport
    }

    fun setAdultPassenger(adultPassenger: Int) = _adultPassenger.postValue(adultPassenger)

    fun setChildPassenger(childPassenger: Int) = _childPassenger.postValue(childPassenger)

    fun setTotalPassenger(totalPassenger: Int) = _totalPassenger.postValue(totalPassenger)

    fun setChosenTicket(chosenTicket: TicketItem) = _chosenTicket.postValue(chosenTicket)

    fun clearChosenTicket() = _chosenTicket.postValue(null)

    fun bookTicket(token: String, data: PostBookingBody): LiveData<BookingTicketResponse?> {
        client.bookTicket(token, data).enqueue(object : Callback<BookingTicketResponse> {
            override fun onResponse(
                call: Call<BookingTicketResponse>,
                response: Response<BookingTicketResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _bookTicket.postValue(result)
                    } else {
                        _bookTicket.postValue(null)
                    }
                }
            }

            override fun onFailure(call: Call<BookingTicketResponse>, t: Throwable) {
                _bookTicket.postValue(null)
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
        return bookTicket
    }

    fun updatePayment(token : String, idTransaksi : String, img : MultipartBody.Part, paymentMethod : RequestBody): LiveData<UpdatePaymentResponse?> {
        client.updatePayment(token, idTransaksi, img, paymentMethod).enqueue(object : Callback<UpdatePaymentResponse>{
            override fun onResponse(
                call: Call<UpdatePaymentResponse>,
                response: Response<UpdatePaymentResponse>
            ) {
                if (response.isSuccessful){
                    val result = response.body()
                    if (result != null){
                        _updatePayment.postValue(result)
                    } else {
                        _updatePayment.postValue(null)
                    }
                }
            }

            override fun onFailure(call: Call<UpdatePaymentResponse>, t: Throwable) {
                _updatePayment.postValue(null)
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
        return updatePayment
    }
}