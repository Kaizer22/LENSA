package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun AboutAppScreen() {
    setSystemUiColor()
    Screen()
}

@Composable
private fun Screen() {
    Column {
        LensaHeader()
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "ВЕРСИЯ",
            style = LensaTheme.typography.header1,
        )
        Text(
            modifier = Modifier.padding(
                start = 32.dp,
                top = 32.dp,
            ),
            text = "1.0.0\nBETA",
            style = LensaTheme.typography.header2,
        )
        VSpace(168.dp)
        LensaIconButton(
            modifier = Modifier.padding(start = 16.dp),
            icon = R.drawable.ic_arrow_back,
            iconSize = 60.dp,
            onClick = {
                //TODO
            },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutAppScreenPreview() {
    LensaTheme {
        Screen()
    }
}