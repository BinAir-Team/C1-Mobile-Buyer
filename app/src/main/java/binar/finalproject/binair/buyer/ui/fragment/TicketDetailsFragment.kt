package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.FragmentTicketDetailsBinding
import binar.finalproject.binair.buyer.databinding.LoginAlertDialogBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketDetailsFragment : Fragment() {
    private lateinit var binding : FragmentTicketDetailsBinding
    private lateinit var clickedTicket : TicketItem
    private lateinit var flightVM : FlightViewModel
    private lateinit var sharedPrefs : SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTicketDetailsBinding.inflate(inflater, container, false)
        binding.toolbar.tvTitlePage.text = "Detail Penerbangan"
        sharedPrefs = requireActivity().getSharedPreferences(dataUser, 0)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromBundle()
        setListener()
    }

    private fun getDataFromBundle() {
//        clickedTicket = arguments?.getParcelable("clickedTicket")!!
        flightVM.getChosenTicket().observe(viewLifecycleOwner){
            if (it != null) {
                clickedTicket = it
                setDataToView()
            }
        }
    }

    private fun setDataToView() {
        val oldDate = clickedTicket.date.substring(0, 10)
        val formatedDate = oldDate.substring(8, 10) + "/" + oldDate.substring(5, 7) + "/" + oldDate.substring(0, 4)
        clickedTicket.date = formatedDate
        binding.ticket = clickedTicket
    }

    private fun setListener() {
        binding.btnPesan.setOnClickListener {
            if(sharedPrefs.getString("token", null) == null) {
                displayLoginAlert()
            } else {
                val action = TicketDetailsFragmentDirections.actionTicketDetailsFragmentToBookingFragment(clickedTicket.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun displayLoginAlert(){
        val builder = AlertDialog.Builder(context)
        val alertDialog = builder.create()
        val dialogView = layoutInflater.inflate(R.layout.login_alert_dialog, null)
        val dialogBinding = LoginAlertDialogBinding.bind(dialogView)
        alertDialog.setView(dialogView)
        alertDialog.create()
        dialogBinding.btnLoginAlert.setOnClickListener {
            findNavController().navigate(R.id.action_ticketDetailsFragment_to_loginFragment)
            alertDialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
//            findNavController().popBackStack()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}
