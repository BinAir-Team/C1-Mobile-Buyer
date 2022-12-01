package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import binar.finalproject.binair.buyer.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.binair.buyer.data.model.News
import binar.finalproject.binair.buyer.data.model.NewsViewModel
import binar.finalproject.binair.buyer.databinding.FragmentCarouselBinding
import binar.finalproject.binair.buyer.ui.adapter.HeadlineViewPager


class CarouselFragment : Fragment() {
    lateinit var binding : FragmentCarouselBinding
    lateinit var newsVM : NewsViewModel
    lateinit var viewPagerAdapter: HeadlineViewPager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarouselBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carouselset()
        newsVM = ViewModelProvider(this)[NewsViewModel::class.java]
        newsVM.getHeadlinesData()
        newsVM.headlineListLiveData.observe(viewLifecycleOwner, Observer {
            viewPagerAdapter.setHeadlineNewsData(it as ArrayList<News>)
        })
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
}