package com.example.finapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}