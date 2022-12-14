package binar.finalproject.binair.buyer.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.databinding.ForgetPasswordDialogBinding
import binar.finalproject.binair.buyer.databinding.FragmentLoginBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var userVM: UserViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var showOneTapUI = true
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        sharedPrefs = requireActivity().getSharedPreferences(dataUser, 0)
        editor = sharedPrefs.edit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        validateInput()
        auth = FirebaseAuth.getInstance()
        initGoogleAuth()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.GONE
    }

    private fun setListener() {
        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            btnSignin.setOnClickListener {
                signInUser()
            }
            btnLoginGoogle.setOnClickListener {
                loginUsingGoogle()
            }
            tvLupaPassword.setOnClickListener {
                forgetPassword()
            }
        }
    }

    private fun emailIsEmpty(): Boolean {
        return binding.emailInput.text.toString().isEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun passwordIsEmpty(): Boolean {
        return binding.passwordInput.text.toString().isEmpty()
    }

    private fun validateInput(): Boolean {
        var isValid = true
        binding.apply {
            emailInput.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (emailIsEmpty()) {
                        emailInput.error = "Email tidak boleh kosong"
                        isValid = false
                    }
                }
            }
            emailInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (!isValidEmail(s.toString())) {
                        isValid = false
                        emailInput.error = "Email tidak valid"
                    }
                }
            })
            passwordInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if (passwordIsEmpty()) {
                            passwordInput.error = "Password tidak boleh kosong"
                            isValid = false
                        }
                    }
                }
            })
        }

        isValid = !(passwordIsEmpty() or emailIsEmpty())
        return isValid
    }

    private fun signInUser() {
        val email = binding.emailInput.text.toString()
        val pass = binding.passwordInput.text.toString()
        if (validateInput()) {
            observeLoginResult(email, pass)
        }else{
            Toast.makeText(requireContext(), "Email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeLoginResult(email: String, pass: String) {
        showLoading(true)
        userVM.loginUser(email, pass).observe(viewLifecycleOwner) { it ->
            if (it?.data != null) {
                showLoading(false)
                saveUserToSharedPref(it.data.id,it.data.firstname,it.data.lastname,it.data.accessToken)
            } else {
                userVM.getLoginErrorMessage().observe(viewLifecycleOwner) {
                    showLoading(false)
                    val errMessage = it
                    if (errMessage.contains("Email does not exist")) {
                        Toast.makeText(
                            requireContext(),
                            "Silahkan registrasi terlebih dahulu",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (errMessage.contains("Email not verified, check your email!")) {
                        Toast.makeText(
                            requireContext(),
                            "Email belum terverifikasi",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (errMessage.contains("Password is incorrect")) {
                        Toast.makeText(context, "Email atau password salah", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun initGoogleAuth() {
        oneTapClient = Identity.getSignInClient(requireContext())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun loginUsingGoogle() {
        showLoadingGoogle(true)
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    showLoadingGoogle(false)
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("LOGIN GOOGLE", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                e.localizedMessage?.let { Log.d("LOGIN GOOGLE", it) }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            loginGoogleAPI(idToken)
                        }
                        else -> {
                            Log.d("LoginGoogle", "No ID token or password!")
                        }
                    }

                } catch (e: ApiException) {
                    // ...
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d("LoginGoogle", "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d("LoginGoogle", "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(
                                "LoginGoogle", "Couldn't get credential from result." +
                                        " (${e.localizedMessage})"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loginGoogleAPI(tokenGoogle : String){
        showLoading(true)
        userVM.loginGoogle(tokenGoogle).observe(viewLifecycleOwner){ it ->
            if (it != null){
                showLoading(false)
                saveUserToSharedPref(it.data.id,it.data.firstname,it.data.lastname,it.data.accessToken)
            }else{
                userVM.getLoginErrorMessage().observe(viewLifecycleOwner){
                    showLoading(false)
                    val errMessage = it
                    if (errMessage.contains("Email does not exist")){
                        Toast.makeText(requireContext(),"Silahkan registrasi terlebih dahulu",Toast.LENGTH_SHORT).show()
                    }else if (errMessage.contains("Email not verified, check your email!")){
                        Toast.makeText(requireContext(),"Email belum terverifikasi",Toast.LENGTH_SHORT).show()
                    }else if (errMessage.contains("Password is incorrect")){
                        Toast.makeText(context,"Email atau password salah",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUserToSharedPref(idUser : String, firstName : String, lastName : String, token : String){
        with(editor) {
            val namaLengkap = "$firstName $lastName"
            putString("token", token)
            putString("idUser", idUser)
            putString("namaLengkap", namaLengkap)
            putBoolean("isLogin", true)
            putBoolean("isValidToken", true)
            apply()
        }
        gotoHome()
    }

    private fun forgetPassword(){
        val builder = AlertDialog.Builder(context)
        val alertDialog = builder.create()
        val dialogView = layoutInflater.inflate(R.layout.forget_password_dialog, null)
        val dialogBinding = ForgetPasswordDialogBinding.bind(dialogView)
        alertDialog.setView(dialogView)
        alertDialog.create()
        dialogBinding.btnSend.setOnClickListener {
            val email = dialogBinding.etEmail.text.toString()
            userVM.forgetPassword(email).observe(viewLifecycleOwner){ it ->
                if (it != null){
                    Toast.makeText(context,"Silahkan cek email anda",Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            alertDialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun gotoHome() {
        findNavController().navigateSafe(R.id.action_loginFragment_to_homeFragment)
    }

    private fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
        val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
        currentDestination?.let { node ->
            val currentNode = when (node) {
                is NavGraph -> node
                else -> node.parent
            }
            if (destinationId != 0) {
                currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
            }
        }
    }

    private fun Int?.orEmpty(default: Int = 0): Int {
        return this ?: default
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.containerLoading.visibility = View.VISIBLE
        } else {
            binding.containerLoading.visibility = View.GONE
        }
    }

    private fun showLoadingGoogle(status: Boolean) {
        if (status) {
            binding.containerLoadingGoogle.visibility = View.VISIBLE
        } else {
            binding.containerLoadingGoogle.visibility = View.GONE
        }
    }
    
    companion object {
        private const val REQ_ONE_TAP = 2
    }
}