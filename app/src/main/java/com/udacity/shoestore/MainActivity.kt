package com.udacity.shoestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.menu.MenuView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.databinding.ActivityMainBinding
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        findNavController()

        configureActionBar()

        // Timber.plant(Timber.DebugTree())   // we are doing this in the application subclass - .application.ShoeStoreApplication
    }

    /*
        We need this extra step since we are using "FragmentContainerView" in our activity layout - instead of <fragment>
        Using navController = this.findNavController(R.id.navHost) - throws an exception: MainActivity does not have a NavController set
        See: https://stackoverflow.com/questions/59275009/fragmentcontainerview-using-findnavcontroller/59275182#59275182 and
        https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set
    */
    private fun findNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController

        // navController = this.findNavController(R.id.navHost) -
        // calling findNavController in onCreate throws an Exception when NavHostFragment is added via FragmentContainerView
    }

    private fun configureActionBar(){

        setSupportActionBar(binding.toolbar)    // execute before linking action-bar to nav-controller

        /*  "up" button should be visible only on Instructions, ShoeList and ShoeDetails screens
            on the rest of the screens, the back-stack will be empty - the only option to navigate
            away without using fragment-buttons is to click "Logout" from the Overflow menu
         */
        // appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration = AppBarConfiguration.Builder(
                                R.id.loginFragment,                         // Up button will not be visible on these screens
                                R.id.welcomeFragment).build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)


        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->

            // prevent the Logout option from appearing anywhere except the ShoeList screen

            binding.toolbar.menu.findItem(R.id.loginFragment)?.isVisible = nd.id == R.id.shoeListFragment

            /*
                Hide the "Up" button when the back-stack is empty -
                so you don't get kicked out of the App from the ShoeList screen
                when you navigate to it after all screens have been popped-off
            */
            if(nc.previousBackStackEntry == null) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)

        // keep overflow menu hidden when app starts
        menu?.findItem(R.id.loginFragment)?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController)
                ||  super.onOptionsItemSelected(item)
    }

}
