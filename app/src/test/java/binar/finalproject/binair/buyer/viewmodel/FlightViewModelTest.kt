package binar.finalproject.binair.buyer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.model.Quantity
import binar.finalproject.binair.buyer.data.model.TravelerItem
import binar.finalproject.binair.buyer.data.response.BookingTicketResponse
import binar.finalproject.binair.buyer.data.response.GetTicketByIdResponse
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.repository.FlightRepository
import binar.finalproject.binair.buyer.utils.DataDummy
import binar.finalproject.binair.buyer.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FlightViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var flightRepo: FlightRepository
    private lateinit var flightVM : FlightViewModel

    @Before
    fun setUP(){
        flightVM = FlightViewModel(flightRepo)
    }

    @Test
    fun `Search ticket not null`(){
        val dummyMutLD = MutableLiveData<List<TicketItem>?>()
        dummyMutLD.value = DataDummy.searchTicketFound()
        val expectedResponse: LiveData<List<TicketItem>?> = dummyMutLD
        Mockito.`when`(flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01",null,"oneway")).thenReturn(expectedResponse)

        val actualResp = flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01",null,"oneway").getOrAwaitValue()
        Mockito.verify(flightRepo).callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01",null,"oneway")
        Assert.assertNotNull(actualResp)
    }

    @Test
    fun `Success search ticket oneway`(){
        val dummyMutLD = MutableLiveData<List<TicketItem>?>()
        dummyMutLD.value = DataDummy.searchTicketFound()
        val expectedResponse: LiveData<List<TicketItem>?> = dummyMutLD
        Mockito.`when`(flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","oneway")).thenReturn(expectedResponse)

        val actualResp = flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","oneway").getOrAwaitValue()
        Mockito.verify(flightRepo).callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","oneway")
        Assert.assertEquals(expectedResponse.value,actualResp)
    }

    @Test
    fun `Success search ticket roundtrip`(){
        val dummyMutLD = MutableLiveData<List<TicketItem>?>()
        dummyMutLD.value = DataDummy.successSearchTicketRoundTrip()
        val expectedResponse: LiveData<List<TicketItem>?> = dummyMutLD
        Mockito.`when`(flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","roundtrip")).thenReturn(expectedResponse)

        val actualResp = flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","roundtrip").getOrAwaitValue()
        Mockito.verify(flightRepo).callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","roundtrip")
        Assert.assertEquals(expectedResponse.value,actualResp)
    }

    @Test
    fun `Search ticket not found`(){
        val dummyMutLD = MutableLiveData<List<TicketItem>?>()
        dummyMutLD.value = DataDummy.searchTicketNotFound()
        val expectedResponse: LiveData<List<TicketItem>?> = dummyMutLD
        Mockito.`when`(flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","roundtrip")).thenReturn(expectedResponse)

        val actualResp = flightVM.callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","roundtrip").getOrAwaitValue()
        Mockito.verify(flightRepo).callGetTicketBySearch("Surabaya","Juanda","Makasaar","Sultan Hasanuddin","2023-01-01","2023-01-10","roundtrip")
        Assert.assertEquals(expectedResponse.value,actualResp)
    }

    @Test
    fun `Success get ticket by id`(){
        val dummyMutLD = MutableLiveData<GetTicketByIdResponse>()
        dummyMutLD.value = DataDummy.getTicketById()
        val expectedResponse: LiveData<GetTicketByIdResponse?> = dummyMutLD
        Mockito.`when`(flightVM.getTicketById("31588def-1557-4a4c-8083-a2a3010790f5")).thenReturn(expectedResponse)

        val actualResp = flightVM.getTicketById("31588def-1557-4a4c-8083-a2a3010790f5").getOrAwaitValue()
        Mockito.verify(flightRepo).getTicketById("31588def-1557-4a4c-8083-a2a3010790f5")
        Assert.assertEquals(expectedResponse.value,actualResp)
    }

    @Test
    fun `Success book ticket`(){
        val listTrav = listOf(
            TravelerItem(
                datebirth = "19 Aug 94",
                idCard = "3692581472589",
                name = "Tulus",
                nationality = "Indonesia ",
                noKtp = "3692581472589",
                surname = "Tulus",
                tittle = "Tuan",
                type = "adult"
            ), TravelerItem(
                datebirth = "19 Aug 02",
                idCard = null,
                name = "Legowo",
                nationality = "Indonesia ",
                noKtp = null,
                surname = "Legowo",
                tittle = "",
                type = "child"
            )
        )
        val dataBooking = PostBookingBody("432762ac-0aa1-44ff-81dc-4d0602eac592",
            Quantity(1, 1),listTrav)
        val dummyMutLD = MutableLiveData<BookingTicketResponse>()
        dummyMutLD.value = DataDummy.bookTicketSuccess()
        val expectedResponse: LiveData<BookingTicketResponse?> = dummyMutLD
        Mockito.`when`(flightVM.bookTicket("1",dataBooking)).thenReturn(expectedResponse)

        val actualResp = flightVM.bookTicket("1",dataBooking).getOrAwaitValue()
        Mockito.verify(flightRepo).bookTicket("1",dataBooking)
        Assert.assertEquals(expectedResponse.value,actualResp)
    }

}