package binar.finalproject.binair.buyer.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navBuyTicket -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navHistory -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_ticketHistoryFragment2)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navWishlist -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_wishlistFragment2)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navProfile -> {
                    Navigation.findNavController(this,R.id.fragmentContainer).navigate(R.id.action_global_profileFragment2)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }
}