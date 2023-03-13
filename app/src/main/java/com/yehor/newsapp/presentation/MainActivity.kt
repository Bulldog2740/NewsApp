package com.yehor.newsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yehor.newsapp.R
import com.yehor.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    private val destinationChangeListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.isVisible = destination.id != R.id.articleFragment
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().let { binding.bottomNavigationView.setupWithNavController(it) }
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener(destinationChangeListener)
        return navHostFragment.navController
    }

    fun setBottomBarVisibility(hide: Boolean) {
        binding.bottomNavigationView.isVisible = hide
    }
}