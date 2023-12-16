package ru.arinae_va.lensa.presentation.common.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


fun todo(context: Context) {
    Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show()
}