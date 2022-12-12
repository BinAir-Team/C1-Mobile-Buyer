package binar.finalproject.binair.buyer.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    lateinit var userVM : UserViewModel
    private lateinit var sharedPrefs : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.GONE
    }

    private fun setListener() {
        binding.apply {
            tvRegister.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            btnSignin.setOnClickListener {
                signInUser()
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

    private fun validateInput() : Boolean {
        var isValid = true
        binding.apply {
            emailInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if(emailIsEmpty()){
                            emailInput.error = "Email tidak boleh kosong"
                            isValid = false
                        }
                    }
                }
            })
            emailInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if(!isValidEmail(s.toString())){
                        isValid = false
                        emailInput.error = "Email tidak valid"
                    }
                }
            })
            passwordInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if(passwordIsEmpty()){
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
        if(validateInput()){
            observeLoginResult(email,pass)
        }
    }

    private fun observeLoginResult(email: String, pass: String) {
        userVM.loginUser(email,pass).observe(viewLifecycleOwner) {
            if (it != null && it.message != "Email does not exist") {
                val namaLengkap = it.data.firstname + " " + it.data.lastname
                editor.putString("token", it.data.accessToken)
                editor.putString("namaLengkap", namaLengkap)
                editor.putBoolean("isLogin", true)
                editor.apply()
                gotoHome()
            }else{
                Toast.makeText(context, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun gotoHome(){
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
        }}

    fun Int?.orEmpty(default: Int = 0): Int {
        return this ?: default
    }
}