package binar.finalproject.binair.buyer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.response.LoginResponse
import binar.finalproject.binair.buyer.data.response.RegisterUserResponse
import binar.finalproject.binair.buyer.repository.UserRepository
import binar.finalproject.binair.buyer.utils.DataDummy
import binar.finalproject.binair.buyer.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepo: UserRepository
    private lateinit var userVM: UserViewModel
    private val dummyRegister = DataDummy.generateUserRegister()

    @Before
    fun setUp() {
        userVM = UserViewModel(userRepo)
    }

    @Test
    fun `Register user success not return null`() {
        val dataRegister = DataRegister(
            firstname = "John",
            lastname = "Doe",
            email = "johndoe@gmail",
            gender = "Laki-laki",
            phone = "08123456789",
            password = "123456",
            confirmPassword = "123456"
        )

        val dummyMutLD = MutableLiveData<RegisterUserResponse>()
        dummyMutLD.value = dummyRegister
        val expectedResponse: LiveData<RegisterUserResponse?> = dummyMutLD
        `when`(userRepo.registerUser(dataRegister)).thenReturn(expectedResponse)

        val actualResp = userVM.registerUser(dataRegister).getOrAwaitValue()
        Mockito.verify(userRepo).registerUser(dataRegister)
        Assert.assertNotNull(actualResp)
    }

    @Test
    fun `Register user success return correct response`() {
        val dataRegister = DataRegister(
            firstname = "John",
            lastname = "Doe",
            email = "johndoe@gmail",
            gender = "Laki-laki",
            phone = "08123456789",
            password = "123456",
            confirmPassword = "123456"
        )

        val dummyMutLD = MutableLiveData<RegisterUserResponse>()
        dummyMutLD.value = dummyRegister
        val expectedResponse: LiveData<RegisterUserResponse?> = dummyMutLD
        `when`(userRepo.registerUser(dataRegister)).thenReturn(expectedResponse)

        val actualResp = userVM.registerUser(dataRegister).getOrAwaitValue()
        Mockito.verify(userRepo).registerUser(dataRegister)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Register failed email empty`(){
        val dataRegister = DataRegister(
            firstname = "John",
            lastname = "Doe",
            email = "",
            gender = "Laki-laki",
            phone = "08123456789",
            password = "123456",
            confirmPassword = "123456"
        )

        val dummyMutLD = MutableLiveData<RegisterUserResponse>()
        dummyMutLD.value = DataDummy.registerFailedEmailPassEmpty()
        val expectedResponse: LiveData<RegisterUserResponse?> = dummyMutLD
        `when`(userRepo.registerUser(dataRegister)).thenReturn(expectedResponse)

        val actualResp = userVM.registerUser(dataRegister).getOrAwaitValue()
        Mockito.verify(userRepo).registerUser(dataRegister)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Register failed password empty`(){
        val dataRegister = DataRegister(
            firstname = "John",
            lastname = "Doe",
            email = "johndoe@gmail.com",
            gender = "Laki-laki",
            phone = "08123456789",
            password = "",
            confirmPassword = ""
        )

        val dummyMutLD = MutableLiveData<RegisterUserResponse>()
        dummyMutLD.value = DataDummy.registerFailedEmailPassEmpty()
        val expectedResponse: LiveData<RegisterUserResponse?> = dummyMutLD
        `when`(userRepo.registerUser(dataRegister)).thenReturn(expectedResponse)

        val actualResp = userVM.registerUser(dataRegister).getOrAwaitValue()
        Mockito.verify(userRepo).registerUser(dataRegister)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Register email invalid`(){
        val dataRegister = DataRegister(
            firstname = "John",
            lastname = "Doe",
            email = "johndoegmail.com",
            gender = "Laki-laki",
            phone = "08123456789",
            password = "123456",
            confirmPassword = "123456"
        )

        val dummyMutLD = MutableLiveData<RegisterUserResponse>()
        dummyMutLD.value = DataDummy.registerFailedEmailInvalid()
        val expectedResponse: LiveData<RegisterUserResponse?> = dummyMutLD
        `when`(userRepo.registerUser(dataRegister)).thenReturn(expectedResponse)

        val actualResp = userVM.registerUser(dataRegister).getOrAwaitValue()
        Mockito.verify(userRepo).registerUser(dataRegister)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Register email already exist`(){
        val dataRegister = DataRegister(
            firstname = "John",
            lastname = "Doe",
            email = "johndoe@gmail.com",
            gender = "Laki-laki",
            phone = "08123456789",
            password = "123456",
            confirmPassword = "123456"
        )

        val dummyMutLD = MutableLiveData<RegisterUserResponse>()
        dummyMutLD.value = DataDummy.registerEmailAlreadyExist()
        val expectedResponse: LiveData<RegisterUserResponse?> = dummyMutLD
        `when`(userRepo.registerUser(dataRegister)).thenReturn(expectedResponse)

        val actualResp = userVM.registerUser(dataRegister).getOrAwaitValue()
        Mockito.verify(userRepo).registerUser(dataRegister)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Register failed if pass and conf pass not same`(){
        val dataRegister = DataRegister(
            firstname = "John",
            lastname = "Doe",
            email = "johndoe@gmail.com",
            gender = "Laki-laki",
            phone = "08123456789",
            password = "123456",
            confirmPassword = "123456"
        )

        val dummyMutLD = MutableLiveData<RegisterUserResponse>()
        dummyMutLD.value = DataDummy.registerFailedPassNotEqual()
        val expectedResponse: LiveData<RegisterUserResponse?> = dummyMutLD
        `when`(userRepo.registerUser(dataRegister)).thenReturn(expectedResponse)

        val actualResp = userVM.registerUser(dataRegister).getOrAwaitValue()
        Mockito.verify(userRepo).registerUser(dataRegister)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Login success`() {
        val dummyMutLD = MutableLiveData<LoginResponse>()
        dummyMutLD.value = DataDummy.loginSuccess()
        val expectedResponse: LiveData<LoginResponse?> = dummyMutLD
        `when`(userRepo.loginUser("johndoe@gmail.com", "123456")).thenReturn(expectedResponse)

        val actualResp = userVM.loginUser("johndoe@gmail.com", "123456").getOrAwaitValue()
        Mockito.verify(userRepo).loginUser("johndoe@gmail.com", "123456")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Login failed email not found`() {
        val dummyMutLD = MutableLiveData<LoginResponse>()
        dummyMutLD.value = DataDummy.loginFailedEmailNotFound()
        val expectedResponse: LiveData<LoginResponse?> = dummyMutLD
        `when`(userRepo.loginUser("lalala@gmail.com","123456")).thenReturn(expectedResponse)

        val actualResp = userVM.loginUser("lalala@gmail.com","123456").getOrAwaitValue()
        Mockito.verify(userRepo).loginUser("lalala@gmail.com","123456")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Login failed password not match`() {
        val dummyMutLD = MutableLiveData<LoginResponse>()
        dummyMutLD.value = DataDummy.loginFailedPassWrong()
        val expectedResponse: LiveData<LoginResponse?> = dummyMutLD
        `when`(userRepo.loginUser("johndoe@gmail.com", "abcdef")).thenReturn(expectedResponse)

        val actualResp = userVM.loginUser("johndoe@gmail.com", "abcdef").getOrAwaitValue()
        Mockito.verify(userRepo).loginUser("johndoe@gmail.com", "abcdef")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Login failed email not verified`() {
        val dummyMutLD = MutableLiveData<LoginResponse>()
        dummyMutLD.value = DataDummy.loginFailedEmailNotVerified()
        val expectedResponse: LiveData<LoginResponse?> = dummyMutLD
        `when`(userRepo.loginUser("john@gmail.com", "abcdef")).thenReturn(expectedResponse)

        val actualResp = userVM.loginUser("john@gmail.com", "abcdef").getOrAwaitValue()
        Mockito.verify(userRepo).loginUser("john@gmail.com", "abcdef")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }
}