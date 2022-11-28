package binar.finalproject.binair.buyer.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import binar.finalproject.binair.buyer.data.Constant.dataPassenger
import binar.finalproject.binair.buyer.databinding.FormTravelerBinding
import binar.finalproject.binair.buyer.databinding.FragmentBookingBinding

class BookingFragment : Fragment() {
    private lateinit var binding : FragmentBookingBinding
    private lateinit var sharedPrefPassenger : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        sharedPrefPassenger = requireActivity().getSharedPreferences(dataPassenger, 0)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showFormTraveler()
    }

    private fun showFormTraveler() {
        val jmlDewasa = sharedPrefPassenger.getInt("jmlDewasa", -1)
        val jmlAnak = sharedPrefPassenger.getInt("jmlAnak", -1)
        val arrFormDewasa = arrayOf(binding.formAdult1, binding.formAdult2, binding.formAdult3, binding.formAdult4, binding.formAdult5, binding.formAdult6, binding.formAdult7)
        val arrFormAnak = arrayOf(binding.formChild1, binding.formChild2, binding.formChild3, binding.formChild4, binding.formChild5, binding.formChild6, binding.formChild7)

        if(jmlDewasa > 0) {
            for (i in 0 until jmlDewasa) {
                (arrFormDewasa[i] as FormTravelerBinding).cvAdult1Container.visibility = View.VISIBLE
                (arrFormDewasa[i] as FormTravelerBinding).labelTipeTraveler.text = "(Dewasa ${i+1})"
            }
        }
        if(jmlAnak > 0){
            for (i in 0 until jmlAnak) {
                (arrFormAnak[i] as FormTravelerBinding).cvAdult1Container.visibility = View.VISIBLE
                (arrFormAnak[i] as FormTravelerBinding).labelTipeTraveler.text = "(Anak ${i+1})"
            }
        }
    }
}