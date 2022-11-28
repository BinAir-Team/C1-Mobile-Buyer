package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.FragmentTicketDetailsBinding

class TicketDetailsFragment : Fragment() {
    private lateinit var binding : FragmentTicketDetailsBinding
    private lateinit var clickedTicket : TicketItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromBundle()
        setDataToView()
        setListener()
    }

    private fun getDataFromBundle() {
        clickedTicket = arguments?.getParcelable("clickedTicket")!!
    }

    private fun setDataToView() {
        val oldDate = clickedTicket.date.substring(0, 10)
        val formatedDate = oldDate.substring(8, 10) + "/" + oldDate.substring(5, 7) + "/" + oldDate.substring(0, 4)
        clickedTicket.date = formatedDate
        binding.ticket = clickedTicket
    }

    private fun setListener() {
        binding.btnPesan.setOnClickListener {
            findNavController().navigate(R.id.action_ticketDetailsFragment_to_bookingFragment)
        }
    }
}
