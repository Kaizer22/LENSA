package ru.arinae_va.lensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IPresenceRepository
import javax.inject.Inject

open class PresenceAwareActivity: ComponentActivity() {
    @Inject
    lateinit var presenceRepository: IPresenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { presenceRepository.setOnline() }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch { presenceRepository.setOffline() }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { presenceRepository.setOnline() }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch { presenceRepository.setOffline() }
    }
}