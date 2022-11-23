package binar.finalproject.binair.buyer.ui.fragment

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import binar.finalproject.binair.buyer.databinding.FragmentHomeBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation()
        setAutoComplete()
    }

    private fun setAutoComplete() {
        val fruits = arrayOf("Apple","Anggur","Apricot", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear")
        //Creating the instance of ArrayAdapter containing list of fruit names
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context!!, R.layout.select_dialog_item, fruits)
        val actv = binding.etFrom
        actv.threshold = 1 //will start working from first character
        actv.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED)
    }

    private fun showBottomNavigation() {
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.VISIBLE
    }
}