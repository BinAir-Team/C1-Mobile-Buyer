package binar.finalproject.binair.buyer.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.local.WishlistDAO
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.remote.APIService
import binar.finalproject.binair.buyer.data.response.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FlightRepository @Inject constructor(var client: APIService, val wishlistDAO: WishlistDAO) {
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
    val bookTicket: LiveData<BookingTicketResponse?> = _bookTicket
    private val _updatePayment = MutableLiveData<UpdatePaymentResponse?>()
    val updatePayment: LiveData<UpdatePaymentResponse?> = _updatePayment
    private val _ticketById = MutableLiveData<GetTicketByIdResponse?>()
    val ticketById: LiveData<GetTicketByIdResponse?> = _ticketById
    private val _userTrans = MutableLiveData<List<TransItem>?>()
    val userTrans: LiveData<List<TransItem>?> = _userTrans
    private val _allPromo = MutableLiveData<List<DataPromo>?>()
    val allPromo : LiveData<List<DataPromo>?> = _allPromo
    private val _allWishlist = MutableLiveData<List<DataWishList>?>()
    val allWishlist : LiveData<List<DataWishList>?> = _allWishlist

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
        client.getTicketBySearch(from, airport_from, to, airport_to, date, type,willFly)
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

    fun callGetAllPromo(): LiveData<List<DataPromo>?>{
        client.getAllPromo().enqueue(object : Callback<AllPromoResponse>{
            override fun onResponse(
                call: Call<AllPromoResponse>,
                response: Response<AllPromoResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _allPromo.postValue(result.data)
                        Log.d("RESULT", "Result : $result")
                    }else{
                        _allPromo.postValue(null)
                    }
                }else{
                    Log.e("Error : ", "onFailed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllPromoResponse>, t: Throwable) {
                Log.e("Error : ", "onFailure: ${t.message}")
            }

        })
        return allPromo
    }

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

    fun getTicketById(id : String): LiveData<GetTicketByIdResponse?> {
        client.getTicketById(id).enqueue(object : Callback<GetTicketByIdResponse>{
            override fun onResponse(
                call: Call<GetTicketByIdResponse>,
                response: Response<GetTicketByIdResponse>
            ) {
                if (response.isSuccessful){
                    val result = response.body()
                    if (result != null){
                        _ticketById.postValue(result)
                    } else {
                        _ticketById.postValue(null)
                    }
                }
            }

            override fun onFailure(call: Call<GetTicketByIdResponse>, t: Throwable) {
                _ticketById.postValue(null)
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
        return ticketById
    }

    fun getUserTrans(token : String) : LiveData<List<TransItem>?> {
        client.getUserTrans(token).enqueue(object : Callback<GetUserTransResponse>{
            override fun onResponse(
                call: Call<GetUserTransResponse>,
                response: Response<GetUserTransResponse>
            ) {
                if(response.isSuccessful){
                    val dataResp = response.body()
                    if(dataResp != null){
                        _userTrans.postValue(dataResp.data as List<TransItem>?)
                    }else{
                        _userTrans.postValue(null)
                        Log.e("Error : ", "onFailed: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetUserTransResponse>, t: Throwable) {
                _userTrans.postValue(null)
                Log.e("Error : ", "onFailure: ${t.message}")
            }

        })
        return userTrans
    }

    fun getAllDataWishlist() : LiveData<List<DataWishList>?> {
        GlobalScope.launch {
            _allWishlist.postValue(wishlistDAO.getWishList())
        }
        return allWishlist
    }

    fun isWishlisted(id : String) : Boolean {
        var result = false
        GlobalScope.launch {
            result = wishlistDAO.isWishlisted(id)
        }
        return result
    }

    fun insertWishlist(wishlist: DataWishList){
        GlobalScope.async {
            wishlistDAO.insertWishList(wishlist)
        }
    }

    fun editWishlist(wishlist: DataWishList) = wishlistDAO.updateWishList(wishlist)

    fun deleteWishlist(wishlist: DataWishList){
        GlobalScope.async {
            wishlistDAO.deleteWishList(wishlist)
        }
    }
}