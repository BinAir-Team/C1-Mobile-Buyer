package binar.finalproject.binair.buyer.ui.fragment

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
    lateinit var userVM: UserViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
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
            emailInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if (emailIsEmpty()) {
                            emailInput.error = "Email tidak boleh kosong"
                            isValid = false
                        }
                    }
                }
            })
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
        return isValid
    }

    private fun signInUser() {
        val email = binding.emailInput.text.toString()
        val pass = binding.passwordInput.text.toString()
        if (validateInput()) {
            observeLoginResult(email, pass)
        }
    }

    private fun observeLoginResult(email: String, pass: String) {
        showLoading(true)
        userVM.loginUser(email, pass).observe(viewLifecycleOwner) { it ->
            if (it != null) {
                showLoading(false)
                val namaLengkap = it.data.firstname + " " + it.data.lastname
                editor.putString("token", it.data.accessToken)
                editor.putString("idUser", it.data.id)
                editor.putString("namaLengkap", namaLengkap)
                editor.putBoolean("isLogin", true)
                editor.putBoolean("isValidToken", true)
                editor.apply()
                gotoHome()
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
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
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
                Log.d("LOGIN GOOGLE", e.localizedMessage)
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val TAG = "LOGIN GOOGLE"
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val secret = credential
                    val username = credential.displayName
                    val email = credential.id
                    val img = credential.profilePictureUri
                    Log.d("RESULT_GOOGLE", "onActivityResult: $idToken $username $email $img")
                    when {
                        idToken != null -> {
                            with(editor) {
                                putString("token", idToken)
                                putString("namaLengkap", username)
                                putString("email", email)
                                putString("img", img.toString())
                                putBoolean("isLogin", true)
                                apply()
                            }
                            gotoHome()
                        }
                        else -> {
                            Log.d(TAG, "No ID token or password!")
                        }
                    }

                } catch (e: ApiException) {
                    // ...
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(
                                TAG, "Couldn't get credential from result." +
                                        " (${e.localizedMessage})"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun gotoHome() {
        findNavController().navigateSafe(R.id.action_loginFragment_to_homeFragment)
    }

    fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
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

    fun Int?.orEmpty(default: Int = 0): Int {
        return this ?: default
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.pbLogin.visibility = View.VISIBLE
        } else {
            binding.pbLogin.visibility = View.GONE
        }
    }
}