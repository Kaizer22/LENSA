package ru.arinae_va.lensa.presentation.common.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun AuthScreen(
    navController: NavController,
){
    setSystemUiColor()
    Screen()
}

@Composable
private fun Screen() {
    Column {
        LensaHeader()
        VSpace(100.dp)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            style = LensaTheme.typography.header1,
            text = "ПРОИЗО\nШЛА\nКАКАЯ-\nТО\nОШИБКА"
        )
        FSpace()
        LensaIconButton(
            modifier = Modifier.padding(start = 16.dp),
            icon = R.drawable.ic_arrow_back,
            iconSize = 60.dp,
            onClick = {}
        )
        VSpace(100.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    LensaTheme {
        Screen()
    }
}