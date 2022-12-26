package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.model.Carousel
import binar.finalproject.binair.buyer.databinding.FragmentCarouselBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.ui.adapter.HeadlineViewPager
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel


class CarouselFragment : Fragment() {
    private lateinit var binding : FragmentCarouselBinding
    private lateinit var flightVM : FlightViewModel
    private lateinit var viewPagerAdapter: HeadlineViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarouselBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        flightVM.getDataCarousel()
        flightVM.carouselListLiveData.observe(viewLifecycleOwner, Observer {
            viewPagerAdapter.setHeadlineNewsData(it as ArrayList<Carousel>)
        })
        carouselset()
        setSwipeUpListener()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.GONE
    }

    private fun carouselset(){
        viewPagerAdapter = HeadlineViewPager(ArrayList())
        binding.vpHeadline.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpHeadline.offscreenPageLimit = 3
        binding.vpHeadline.adapter = viewPagerAdapter
        binding.vpHeadline.setPageTransformer(MarginPageTransformer(50))
        binding.vpHeadline.clipToPadding = false;
        binding.vpHeadline.setPadding(10,10,10,0);
        binding.dotsIndicator.attachTo(binding.vpHeadline)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSwipeUpListener(){
        binding.containerSwipeUp.setOnTouchListener { view, event ->
            when(event.action){
                MotionEvent.ACTION_UP -> {
                    findNavController().navigate(R.id.action_carouselFragment2_to_homeFragment)
                }
            }
//            view.performClick()
            return@setOnTouchListener true
        }
    }
}