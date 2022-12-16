package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.data.FlightRepository
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.data.response.TicketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private val flightRepo : FlightRepository) : ViewModel() {
    fun callGetCityAirport() : LiveData<List<CityAirport>?> = flightRepo.callGetCityAirport()
    fun callGetAllTicket() : LiveData<List<TicketItem>?> = flightRepo.callGetAllTicket()
    fun setAdultPassenger(adultPassenger : Int) = flightRepo.setAdultPassenger(adultPassenger)
    fun getAdultPassenger() : LiveData<Int> = flightRepo.adultPassenger
    fun setChildPassenger(childPassenger : Int) = flightRepo.setChildPassenger(childPassenger)
    fun getChildPassenger() : LiveData<Int> = flightRepo.childPassenger
    fun setTotalPassenger(totalPassenger : Int) = flightRepo.setTotalPassenger(totalPassenger)
    fun getTotalPassenger() : LiveData<Int> = flightRepo.totalPassenger
    fun getAllPromo() : LiveData<List<DataPromo>?> = flightRepo.callGetAllPromo()
}
