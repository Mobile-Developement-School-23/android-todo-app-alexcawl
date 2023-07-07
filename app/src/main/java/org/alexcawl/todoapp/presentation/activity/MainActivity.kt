package org.alexcawl.todoapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.alexcawl.todoapp.R
import org.alexcawl.todoapp.databinding.ActivityMainBinding
import org.alexcawl.todoapp.di.component.MainActivityComponent
import org.alexcawl.todoapp.presentation.ToDoApplication
import org.alexcawl.todoapp.presentation.util.applicationComponent
import org.alexcawl.todoapp.presentation.worker.SyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationController: NavController
    lateinit var activityComponent: MainActivityComponent
        private set

    private val networkConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val syncWork = PeriodicWorkRequestBuilder<SyncWorker>(
        15, TimeUnit.MINUTES,
        14, TimeUnit.MINUTES
    ).setConstraints(networkConstraints).build()

    private val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = this.applicationComponent.mainActivityComponent()
        workManager.enqueue(syncWork)
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