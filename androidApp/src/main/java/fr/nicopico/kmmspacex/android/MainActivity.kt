package fr.nicopico.kmmspacex.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.nicopico.kmmspacex.Greeting
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import fr.nicopico.kmmspacex.android.databinding.ActivityMainBinding
import fr.nicopico.kmmspacex.shared.SpaceXSDK
import fr.nicopico.kmmspacex.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = LaunchesRvAdapter(listOf())

    private val sdk = SpaceXSDK(DatabaseDriverFactory(this))
    private var fetchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.activity_title)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.launchesListRv.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }

        binding.swipeContainer.setOnRefreshListener {
            binding.swipeContainer.isRefreshing = false
            displayLaunches(true)
        }

        displayLaunches(false)
    }

    private fun displayLaunches(needReload: Boolean) {
        if (fetchJob?.isActive == true) return

        binding.progressBar.isVisible = true
        fetchJob = lifecycleScope.launch {
            kotlin
                .runCatching {
                    sdk.getLaunches(needReload)
                }
                .onSuccess {
                    adapter.launches = it
                    adapter.notifyDataSetChanged()
                }
                .onFailure {
                    Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            binding.progressBar.isVisible = false
        }
    }
}
