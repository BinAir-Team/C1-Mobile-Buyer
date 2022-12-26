package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.model.Carousel
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.response.*
import binar.finalproject.binair.buyer.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private val flightRepo : FlightRepository) : ViewModel() {
    var carouselList = arrayListOf(
        Carousel(id = 1,imgUrl = R.drawable.carousel1 ),
        Carousel(id = 2,imgUrl = R.drawable.carousel2 ),
        Carousel(id = 3,imgUrl = R.drawable.carousel3 ),
        Carousel(id = 4,imgUrl = R.drawable.carousel4 ),
    )

    var carouselListLiveData: MutableLiveData<List<Carousel>> = MutableLiveData()
    fun getDataCarousel(){
        val carousels = carouselList
        carouselListLiveData.value = carousels
    }
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
    fun callGetTicketBySearch(from: String, airport_from : String, to: String, airport_to : String, dateStart : String, dateEnd : String?, type : String) : LiveData<List<TicketItem>?> = flightRepo.callGetTicketBySearch(from, airport_from, to, airport_to, dateStart, dateEnd, type)
    fun bookTicket(token : String, data : PostBookingBody) = flightRepo.bookTicket(token, data)
    fun updatePayment(token : String, id : String, img : MultipartBody.Part, method : RequestBody) = flightRepo.updatePayment(token, id, img, method)
    fun getTicketById(id : String) : LiveData<GetTicketByIdResponse?> = flightRepo.getTicketById(id)
    fun getUserTrans(token : String) : LiveData<List<TransItem>?> = flightRepo.getUserTrans(token)
    fun getAllPromo() : LiveData<List<DataPromo>?> = flightRepo.callGetAllPromo()
    // WishList
    fun isWishlisted(id: String, user : String) : Boolean = flightRepo.isWishlisted(id, user)
    fun getAllWishlist(user : String) : LiveData<List<DataWishList>?> = flightRepo.getAllDataWishlist(user)
    fun insertWishList(wishlist: DataWishList, user : String) = flightRepo.insertWishlist(wishlist)
    fun deleteWishList(id: String) = flightRepo.deleteWishlist(id)
    //Notification
    fun GetAllNotification(token : String) : LiveData<List<DataNotif>?> = flightRepo.getAllNotif(token)
    fun UpdateNotification(token : String, id: String?) = flightRepo.updateNotification(token,id)
    fun insertAirport(listAirport : List<CityAirport>) = flightRepo.insertAllAirport(listAirport)
    fun getAirportLocal() : LiveData<List<CityAirport>?> = flightRepo.getAllAirport()
    fun insertPromo(listPromo : List<DataPromo>) = flightRepo.insertPromoLocal(listPromo)
    fun getPromoLocal() : LiveData<List<DataPromo>?> = flightRepo.getAllPromoLocal()
}
