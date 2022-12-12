package binar.finalproject.binair.buyer.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.remote.APIService
import binar.finalproject.binair.buyer.data.response.AllPromoResponse
import binar.finalproject.binair.buyer.data.response.DataPromo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PromoRepositoy @Inject constructor(var apiService: APIService){
    private val _allPromo = MutableLiveData<List<DataPromo>?>()
    val allPromo : LiveData<List<DataPromo>?> = _allPromo

    fun callGetAllPromo(): LiveData<List<DataPromo>?>{
        apiService.getAllPromo().enqueue(object : Callback<AllPromoResponse>{
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
}