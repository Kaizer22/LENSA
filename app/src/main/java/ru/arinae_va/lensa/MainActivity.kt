package ru.arinae_va.lensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ru.arinae_va.lensa.presentation.navigation.LensaNavGraph
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LensaTheme {
                LensaNavGraph()
            }
        }
    }
}
