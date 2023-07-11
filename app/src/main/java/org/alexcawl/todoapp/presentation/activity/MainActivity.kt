package org.alexcawl.todoapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.WorkManager
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.ActivityMainBinding
import org.alexcawl.todoapp.di.component.MainActivityComponent
import org.alexcawl.todoapp.presentation.util.applicationComponent
import org.alexcawl.todoapp.presentation.worker.SyncWorker

/**
 * Application main activity
 * @see SyncWorker
 * @see WorkManager
 * */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationController: NavController
    lateinit var activityComponent: MainActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = this.applicationComponent.mainActivityComponent()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navigationController = navHostFragment.navController
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }
}