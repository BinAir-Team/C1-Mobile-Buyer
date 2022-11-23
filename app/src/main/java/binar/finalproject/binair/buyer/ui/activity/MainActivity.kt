package binar.finalproject.binair.buyer.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import binar.finalproject.binair.buyer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}