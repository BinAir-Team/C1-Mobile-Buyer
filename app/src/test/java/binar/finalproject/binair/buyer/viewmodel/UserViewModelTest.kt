package binar.finalproject.binair.buyer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.data.response.*
import binar.finalproject.binair.buyer.repository.UserRepository
import binar.finalproject.binair.buyer.utils.DataDummy
import binar.finalproject.binair.buyer.utils.getOrAwaitValue
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
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
            firstname = "Richard",
            lastname = "Lois",
            email = "richardlois1@gmail.com",
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
            firstname = "Richard",
            lastname = "Lois",
            email = "richardlois1@gmail.com",
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
            firstname = "Richard",
            lastname = "Lois",
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
            firstname = "Richard",
            lastname = "Lois",
            email = "richardlois1@gmail.com",
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
            firstname = "Richard",
            lastname = "Lois",
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
            firstname = "Richard",
            lastname = "Lois",
            email = "richardlois1@gmail.com",
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
            firstname = "Richard",
            lastname = "Lois",
            email = "richardlois1@gmail.com",
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
        `when`(userRepo.loginUser("richardlois1@gmail.com", "123456")).thenReturn(expectedResponse)

        val actualResp = userVM.loginUser("richardlois1@gmail.com", "123456").getOrAwaitValue()
        Mockito.verify(userRepo).loginUser("richardlois1@gmail.com", "123456")
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
        `when`(userRepo.loginUser("richardlois1@gmail.com", "abcdef")).thenReturn(expectedResponse)

        val actualResp = userVM.loginUser("richardlois1@gmail.com", "abcdef").getOrAwaitValue()
        Mockito.verify(userRepo).loginUser("richardlois1@gmail.com", "abcdef")
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

    @Test
    fun `Update profile success`(){
        val firstName = "Richard".toRequestBody()
        val lastName = "Lois".toRequestBody()
        val gender = "Laki-laki".toRequestBody()
        val phone = "08123456789".toRequestBody()
        val image = mock(MultipartBody.Part::class.java)
        val dummyMutLD = MutableLiveData<UpdateUserResponse>()
        dummyMutLD.value = DataDummy.updateUserSuccess()
        val expectedResponse: LiveData<UpdateUserResponse?> = dummyMutLD
        `when`(userRepo.updateUser("1",firstName,lastName,gender,phone,image)).thenReturn(expectedResponse)

        val actualResp = userVM.updateUser("1",firstName,lastName,gender,phone,image).getOrAwaitValue()
        Mockito.verify(userRepo).updateUser("1",firstName,lastName,gender,phone,image)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Update profile without image success`(){
        val firstName = "Richard".toRequestBody()
        val lastName = "Lois".toRequestBody()
        val gender = "Laki-laki".toRequestBody()
        val phone = "08123456789".toRequestBody()

        val dummyMutLD = MutableLiveData<UpdateUserResponse>()
        dummyMutLD.value = DataDummy.updateUserSuccess()
        val expectedResponse: LiveData<UpdateUserResponse?> = dummyMutLD
        `when`(userRepo.updateUserWithoutImage("1",firstName,lastName,gender,phone)).thenReturn(expectedResponse)

        val actualResp = userVM.updateUserWithoutImage("1",firstName,lastName,gender,phone).getOrAwaitValue()
        Mockito.verify(userRepo).updateUserWithoutImage("1",firstName,lastName,gender,phone)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Update profile failed user not found`(){
        val firstName = "Richard".toRequestBody()
        val lastName = "Lois".toRequestBody()
        val gender = "Laki-laki".toRequestBody()
        val phone = "08123456789".toRequestBody()
        val image = mock(MultipartBody.Part::class.java)
        val dummyMutLD = MutableLiveData<UpdateUserResponse>()
        dummyMutLD.value = DataDummy.updateUserSuccess()
        val expectedResponse: LiveData<UpdateUserResponse?> = dummyMutLD
        `when`(userRepo.updateUser("xx",firstName,lastName,gender,phone,image)).thenReturn(expectedResponse)

        val actualResp = userVM.updateUser("xx",firstName,lastName,gender,phone,image).getOrAwaitValue()
        Mockito.verify(userRepo).updateUser("xx",firstName,lastName,gender,phone,image)
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Change password success`(){
        val dummyMutLD = MutableLiveData<UpdateUserPasswordResponse>()
        dummyMutLD.value = DataDummy.changePassSuccess()
        val expectedResponse: LiveData<UpdateUserPasswordResponse?> = dummyMutLD
        `when`(userRepo.updatePassword("1","123456","johndoe1","johndoe1")).thenReturn(expectedResponse)

        val actualResp = userVM.updatePassword("1","123456","johndoe1","johndoe1").getOrAwaitValue()
        Mockito.verify(userRepo).updatePassword("1","123456","johndoe1","johndoe1")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Change password failed old pass wrong`(){
        val dummyMutLD = MutableLiveData<UpdateUserPasswordResponse>()
        dummyMutLD.value = DataDummy.changePassSuccess()
        val expectedResponse: LiveData<UpdateUserPasswordResponse?> = dummyMutLD
        `when`(userRepo.updatePassword("1","zzzzzz","johndoe1","johndoe1")).thenReturn(expectedResponse)

        val actualResp = userVM.updatePassword("1","zzzzzz","johndoe1","johndoe1").getOrAwaitValue()
        Mockito.verify(userRepo).updatePassword("1","zzzzzz","johndoe1","johndoe1")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Change password failed new pass not equal`(){
        val dummyMutLD = MutableLiveData<UpdateUserPasswordResponse>()
        dummyMutLD.value = DataDummy.changePassSuccess()
        val expectedResponse: LiveData<UpdateUserPasswordResponse?> = dummyMutLD
        `when`(userRepo.updatePassword("1","123456","johndoe1","john")).thenReturn(expectedResponse)

        val actualResp = userVM.updatePassword("1","123456","johndoe1","john").getOrAwaitValue()
        Mockito.verify(userRepo).updatePassword("1","123456","johndoe1","john")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Forget password success`(){
        val dummyMutLD = MutableLiveData<ForgetPasswordResponse>()
        dummyMutLD.value = DataDummy.forgetPassSuccess()
        val expectedResponse: LiveData<ForgetPasswordResponse?> = dummyMutLD
        `when`(userRepo.forgetPassword("richardlois1@gmail.com")).thenReturn(expectedResponse)

        val actualResp = userVM.forgetPassword("richardlois1@gmail.com").getOrAwaitValue()
        Mockito.verify(userRepo).forgetPassword("richardlois1@gmail.com")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }

    @Test
    fun `Forget password failed email not found`(){
        val dummyMutLD = MutableLiveData<ForgetPasswordResponse>()
        dummyMutLD.value = DataDummy.forgetPassFailedEmailNotFound()
        val expectedResponse: LiveData<ForgetPasswordResponse?> = dummyMutLD
        `when`(userRepo.forgetPassword("richard@gmail.com")).thenReturn(expectedResponse)

        val actualResp = userVM.forgetPassword("richard@gmail.com").getOrAwaitValue()
        Mockito.verify(userRepo).forgetPassword("richard@gmail.com")
        Assert.assertEquals(expectedResponse.value, actualResp)
    }
}