package binar.finalproject.binair.buyer.utils

import binar.finalproject.binair.buyer.data.response.DataLogin
import binar.finalproject.binair.buyer.data.response.DataUser
import binar.finalproject.binair.buyer.data.response.LoginResponse
import binar.finalproject.binair.buyer.data.response.RegisterUserResponse

object DataDummy {
    fun generateUserLogin() : DataLogin {
        return DataLogin(
            id = "1",
            firstname = "John",
            lastname = "Doe",
            email = "johndoe@gmail",
            gender = "Laki-laki",
            phone = "08123456789",
            role = "member",
            profileImage = "image",
            accessToken = "1"
        )
    }

    fun generateUserRegister() : RegisterUserResponse {
        val dtUser =  DataUser(id = "1", firstname = "John", lastname = "Doe", email = "johndoe@gmail", gender = "Laki-laki", phone = "08123456789", role = "member", password = "123456")
        return RegisterUserResponse(
            status = "success",
            message = "Register success",
            data = dtUser
        )
    }

    fun registerFailedEmailPassEmpty() : RegisterUserResponse {
        return RegisterUserResponse(
            status = "error",
            message = "Email and password is required",
            data = null
        )
    }

    fun registerFailedEmailInvalid() : RegisterUserResponse {
        return RegisterUserResponse(
            status = "error",
            message = "Email format is invalid",
            data = null
        )
    }

    fun registerEmailAlreadyExist() : RegisterUserResponse {
        return RegisterUserResponse(
            status = "error",
            message = "Email already exist",
            data = null
        )
    }

    fun registerFailedPassNotEqual() : RegisterUserResponse{
        return RegisterUserResponse(
            status = "error",
            message = "Password and confirm password does not match",
            data = null
        )
    }

    fun loginSuccess() : LoginResponse {
        return LoginResponse(
            status = "success",
            message = "Login success",
            data = generateUserLogin()
        )
    }

    fun loginFailedEmailNotFound() : LoginResponse {
        return LoginResponse(
            status = "error",
            message = "Email does not exist",
            data = null
        )
    }

    fun loginFailedPassWrong() : LoginResponse {
        return LoginResponse(
            status = "error",
            message = "Password is incorrect",
            data = null
        )
    }

    fun loginFailedEmailNotVerified() : LoginResponse {
        return LoginResponse(
            status = "error",
            message = "Email not verified, check your email!",
            data = null
        )
    }
}