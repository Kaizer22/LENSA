package ru.arinae_va.lensa.presentation.feature.profile.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor

@Composable
fun ProfileScreen(
    
    navController: NavController,
    viewModel: ProfileViewModel,
){
    setSystemUiColor()
    Screen()
}

@Composable
private fun Screen() {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {

}