package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.makeNotification
import binar.finalproject.binair.buyer.data.response.DataNotif
import binar.finalproject.binair.buyer.databinding.FragmentNotificationBinding
import binar.finalproject.binair.buyer.socketio.SocketHandler
import binar.finalproject.binair.buyer.ui.adapter.NotificationAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var flightVM : FlightViewModel
    private lateinit var pop : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        binding.toolbar.tvTitlePage.text = "Notifikasi"
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initSocketIO()
        getNotification()
        setListener()
    }

    private fun setListener() {
        pop = arguments?.getString("pop", null)!!
//        binding.toolbar.btnBack.setOnClickListener() {
//            if (pop == "home"){
//                findNavController().navigate(R.id.action_global_homeFragment)
//            }
//            else
//                findNavController().navigate(R.id.action_global_homeFragment)
//        }
    }

    private fun initSocketIO(){
        val idUser = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("idUser", null)
        SocketHandler.setSocket()
        if(idUser != null){
            val mSocket = SocketHandler.getSocket()
            mSocket.connect()
            mSocket.emit("create",idUser)
            mSocket.on("notify-update"){
                if(it[0] != null){
                    makeNotification("Binair",it[0].toString(), requireContext())
                }
            }
        }
    }

    private fun getNotification() {
        var res : List<DataNotif>? = null
        val token = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("token", null)
        if (token != null) {
            flightVM.GetAllNotification("Bearer $token").observe(viewLifecycleOwner){
                if (it != null) {
                    res = it
                    setDataToRecView(it.reversed())
//                    showLoading(false)
                }
            }
        }
    }

    private fun setDataToRecView(data: List<DataNotif>) {
        val adapter = NotificationAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = layoutManager

        adapter.onupdate = {
            val token = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("token", null)
            Toast.makeText(requireContext(), it.id, Toast.LENGTH_SHORT).show()
            flightVM.UpdateNotification("Bearer $token",it.id).observe(viewLifecycleOwner){
                if (it != null) {
                    if(it.status == 200){
                    }
                }else{
                    Toast.makeText(requireContext(), "Pembayaran gagal $", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}