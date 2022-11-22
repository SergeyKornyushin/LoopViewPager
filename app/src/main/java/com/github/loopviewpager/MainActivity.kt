package com.github.loopviewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.loopviewpager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var _nullablePageIndicatorsView: PageIndicatorsView? = binding.pageIndicatorsView
    private val pageIndicatorsView get() = _nullablePageIndicatorsView!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}