package com.wipro.shopease.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wipro.shopease.R
import com.wipro.shopease.common.mainNav
import com.wipro.shopease.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var mainActivityBinding: ActivityMainBinding
    private var doubleBackToExitPressedOnce: Boolean = false
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initNav()
        initNavigationDrawer()
        iniClick()
    }

    private fun iniClick() {
        with(mainActivityBinding) {
            toolbar.imgToolbarStart.setOnClickListener {
                if (navController.currentDestination?.id == R.id.homeFragment) {
                    mainActivityBinding.drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    mainNav.popBackStack()
                }
            }
        }
    }

    // region navigation
    private fun initNav() {
        initializeNavigation()
        navigationListener()
    }

    fun getForegroundFragment(): Fragment? {
        return try {
            // initialize navigation
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navigationFragment) as NavHostFragment
            navHostFragment.childFragmentManager.fragments[0]
        } catch (e: Exception) {
            null
        }
    }

    private fun initializeNavigation() {
        // initialize navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun navigationListener() {
        navController.addOnDestinationChangedListener { navController, _, _ ->
            // toolbar setup
            with(mainActivityBinding.toolbar) {
                imgToolbarStart.setImageResource(R.drawable.ic_back)
                when (navController.currentDestination?.id) {
                    R.id.homeFragment -> {
                        imgToolbarStart.setImageResource(R.drawable.ic_menu)

                        toolbarLayout.visibility = View.VISIBLE
                        imgToolbarStart.visibility = View.VISIBLE
                        tvTitle.visibility = View.GONE
                    }

                    R.id.productFragment -> {
                        toolbarLayout.visibility = View.VISIBLE
                        imgToolbarStart.visibility = View.VISIBLE
                        tvTitle.visibility = View.VISIBLE
                    }

                    else -> {
                        toolbarLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
    // endregion

    // region navigation drawer
    private fun initNavigationDrawer() {
        mainActivityBinding.navigationView.setupWithNavController(navController)
        mainActivityBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    // endregion

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination!!.id == R.id.homeFragment) {
            if (doubleBackToExitPressedOnce) {
                finish()
                return
            }
            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                "${getString(R.string.press_again_to_close)} ${getString(R.string.app_name)}",
                Toast.LENGTH_SHORT
            ).show()

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2500)
        } else {
            super.onBackPressed()
        }
    }
}