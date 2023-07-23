package com.example.todolist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todolist.R
import com.example.todolist.app.App
import com.example.todolist.databinding.ActivityHostBinding
import com.example.todolist.utils.CheckingPermission
import com.example.todolist.utils.SyncWorkerManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * HostActivity - главная(root) Activity приложения.
 */
class HostActivity : AppCompatActivity() {

    @Inject
    lateinit var permissionChecker: CheckingPermission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingsOfWorkerManager()
        navigation()
        permissionChecker = CheckingPermission(context = this)
        if (!permissionChecker.checkPermissions()) {
            permissionChecker.requestPermissions()
        }
    }

    private fun navigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        floatingActionBar(navController)

        bottomNavigationView.menu.getItem(1).isEnabled = false
        val appBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.addFragmentButton)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addToDoFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    floatingActionButton.visibility = View.GONE
                    appBar.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    floatingActionButton.visibility = View.VISIBLE
                    appBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun floatingActionBar(navController: NavController) {
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.addFragmentButton)
        floatingActionButton.setOnClickListener {
            navController.navigate(R.id.addToDoFragment, null, options)
        }
    }

    private fun settingsOfWorkerManager() {
        val repeatInterval = TIME_OF_REPEAT
        val repeatIntervalTimeUnit = TimeUnit.HOURS
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorkerManager>(repeatInterval, repeatIntervalTimeUnit)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }

    companion object {
        const val TIME_OF_REPEAT = 8L
    }
}