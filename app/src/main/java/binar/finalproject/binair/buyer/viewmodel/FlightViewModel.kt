package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.data.FlightRepository
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.data.response.TicketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private val flightRepo : FlightRepository) : ViewModel() {
    fun callGetCityAirport() : LiveData<List<CityAirport>?> = flightRepo.callGetCityAirport()
    fun setAdultPassenger(adultPassenger : Int) = flightRepo.setAdultPassenger(adultPassenger)
    fun getAdultPassenger() : LiveData<Int> = flightRepo.adultPassenger
    fun setChildPassenger(childPassenger : Int) = flightRepo.setChildPassenger(childPassenger)
    fun getChildPassenger() : LiveData<Int> = flightRepo.childPassenger
    fun setTotalPassenger(totalPassenger : Int) = flightRepo.setTotalPassenger(totalPassenger)
    fun getTotalPassenger() : LiveData<Int> = flightRepo.totalPassenger
    fun setChosenTicket(chosenTicket : TicketItem) = flightRepo.setChosenTicket(chosenTicket)
    fun getChosenTicket() : LiveData<TicketItem?> = flightRepo.chosenTicket
    fun clearChosenTicket() = flightRepo.clearChosenTicket()
    fun callGetAllTicket() : LiveData<List<TicketItem>?> = flightRepo.callGetAllTicket()
    fun callGetTicketBySearch(from: String, airport_from : String, to: String, airport_to : String, date : String, type : String) : LiveData<List<TicketItem>?> = flightRepo.callGetTicketBySearch(from, airport_from, to, airport_to, date, type)
    fun bookTicket(token : String, data : PostBookingBody) = flightRepo.bookTicket(token, data)
    fun updatePayment(token : String, id : String, img : MultipartBody.Part, method : RequestBody) = flightRepo.updatePayment(token, id, img, method)
}
