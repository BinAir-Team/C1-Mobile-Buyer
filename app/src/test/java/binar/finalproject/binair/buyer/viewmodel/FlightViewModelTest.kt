package binar.finalproject.binair.buyer.viewmodel

import binar.finalproject.binair.buyer.repository.UserRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class FlightViewModelTest{
    @Mock
    private lateinit var userRepo: UserRepository
    private lateinit var userVM: UserViewModel

    @Before
    fun setUP(){
        userVM = UserViewModel(userRepo)
    }

    @Test
    fun `Success search ticket`(){

    }


}