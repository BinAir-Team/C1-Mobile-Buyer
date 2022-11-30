package binar.finalproject.binair.buyer.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
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
                    findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navProfile -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_profileFragment2)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }
}