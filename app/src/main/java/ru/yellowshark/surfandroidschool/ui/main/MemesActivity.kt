package ru.yellowshark.surfandroidschool.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.ActivityMemesBinding

class MemesActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMemesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding = ActivityMemesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupBottomNavMenu()
        navController.addOnDestinationChangedListener { _: NavController, navDestination: NavDestination, _: Bundle? ->
            binding.bottomNav.visibility = when (navDestination.id) {
                R.id.destination_meme_detail ->
                    View.GONE
                R.id.destination_create_meme ->
                    View.GONE
                else ->
                    View.VISIBLE
            }
        }
    }

    private fun setupBottomNavMenu() =
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

    override fun onSupportNavigateUp() = navController.navigateUp()
}