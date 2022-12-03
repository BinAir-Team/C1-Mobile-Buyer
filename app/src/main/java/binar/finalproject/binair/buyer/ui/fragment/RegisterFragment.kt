package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.model.DataRegister
import binar.finalproject.binair.buyer.databinding.FragmentRegisterBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    lateinit var userVM : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateInput()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.GONE
    }

    private fun setListener() {
        binding.apply {
            btnDaftar.setOnClickListener {
                registerUser()
            }
        }
    }

    private fun firstNameIsEmpty(): Boolean {
        return binding.firstNameInput.text.toString().isEmpty()
    }

    private fun lastNameIsEmpty(): Boolean {
        return binding.lastNameInput.text.toString().isEmpty()
    }

    private fun emailIsEmpty(): Boolean {
        return binding.emailInput.text.toString().isEmpty()
    }

    private fun passwordIsEmpty(): Boolean {
        return binding.passwordInput.text.toString().isEmpty()
    }

    private fun confirmPasswordIsEmpty(): Boolean {
        return binding.confirmPassInput.text.toString().isEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
//        return email.matches(emailPattern.toRegex())
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateInput() : Boolean{
        var isValid = true

        binding.apply {
            firstNameInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if (firstNameIsEmpty()) {
                            isValid = false
                            firstNameInput.error = "Nama depan wajib diisi"
                        }
                    }
                }
            })
            lastNameInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if (lastNameIsEmpty()) {
                            isValid = false
                            lastNameInput.error = "Nama Belakang wajib diisi"
                        }
                    }
                }
            })
            containerRadioJK.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    if (checkedId == -1) {
                        isValid = false
                    }
                }
            })
            emailInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if (emailIsEmpty()) {
                            isValid = false
                            emailInput.error = "Email wajib diisi"
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
                        if (passwordIsEmpty()) {
                            isValid = false
                            passwordInput.error = "Password wajib diisi"
                        }
                    }
                }
            })
            confirmPassInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        if (confirmPasswordIsEmpty()) {
                            isValid = false
                            confirmPassInput.error = "Konfirmasi password wajib diisi"
                        }
                    }
                }
            })
        }
        return isValid
    }

    private fun isRadioChecked(): Boolean {
        return binding.containerRadioJK.checkedRadioButtonId != -1
    }

    private fun getGender() : String{
        val checkedRadioButton = binding.containerRadioJK.checkedRadioButtonId
        var gender = ""
        if(isRadioChecked()){
            if(checkedRadioButton == R.id.radioLaki){
                gender = "Laki-laki"
            }else if(checkedRadioButton == R.id.radioPerempuan){
                gender = "Perempuan"
            }
        }
        return gender
    }

    private fun isEqualPassRepassword(pass: String, repass: String): Boolean {
        return pass == repass
    }

    private fun registerUser() {
        val firstName = binding.firstNameInput.text.toString()
        val lastName = binding.lastNameInput.text.toString()
        var gender = getGender()
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val confirmPassword = binding.confirmPassInput.text.toString()
        val phone = binding.phoneInput.text.toString()

        if (!isEqualPassRepassword(password, confirmPassword)) {
            binding.confirmPassInput.error = "Password dan konfirmasi password tidak sama"
        } else if(validateInput()){
            val dataRegis = DataRegister(firstName,lastName,gender,email,password,confirmPassword,phone)
            regisObserveUser(dataRegis)
        }
    }

    private fun regisObserveUser(dataRegis : DataRegister){
        userVM.registerUser(dataRegis).observe(viewLifecycleOwner, Observer {
            if(it != null && it.status == "success"){
                Toast.makeText(context, "Berhasil mendaftar", Toast.LENGTH_SHORT).show()
                gotoLogin()
            }else{
                Toast.makeText(context, "Gagal mendaftar", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun gotoLogin(){
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}