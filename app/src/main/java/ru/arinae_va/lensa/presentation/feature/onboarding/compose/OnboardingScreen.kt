package ru.arinae_va.lensa.presentation.feature.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaPager
import ru.arinae_va.lensa.presentation.common.component.PageModel
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
) {
    setSystemUiColor()
    Screen(onNextClick = viewModel::onNextClick)
}

@Composable
private fun Screen(
    onNextClick: () -> Unit,
) {
    val pageModels = listOf(
        PageModel(
            titleText = stringResource(R.string.onboarding_step_1_title),
            subtitleText = stringResource(R.string.onboarding_step_1_text)
        ),
        PageModel(
            titleText = stringResource(R.string.onboarding_step_2_title),
            subtitleText = stringResource(R.string.onboarding_step_2_text)
        ),
        PageModel(
            titleText = stringResource(R.string.onboarding_step_3_title),
            subtitleText = stringResource(R.string.onboarding_step_3_text)
        ),
        PageModel(
            titleText = stringResource(R.string.onboarding_step_4_title),
            subtitleText = stringResource(R.string.onboarding_step_4_text)
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = LensaTheme.colors.backgroundColor
            )
    ) {
        LensaHeader()
        LensaPager(
            modifier = Modifier.weight(1f),
            pageCount = pageModels.size,
            ) { pageNumber ->
            OnboardingPage(
                model = pageModels[pageNumber],
                onNextButtonClick = onNextClick,
                showNextButton = pageModels.size == pageNumber + 1
            )
        }
        VSpace(h = 40.dp)
    }
}

@Composable
private fun OnboardingPage(
    model: PageModel,
    onNextButtonClick: () -> Unit,
    showNextButton: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        model.titleText?.let {
            Text(
                text = it,
                style = LensaTheme.typography.header2,
                color = LensaTheme.colors.textColor,
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        model.subtitleText?.let {
            Text(
                text = it,
                style = LensaTheme.typography.text,
                color = LensaTheme.colors.textColor,
            )
        }
        if (showNextButton) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LensaIconButton(
                    icon = R.drawable.ic_arrow_forward,
                    iconSize = 60.dp,
                    onClick = onNextButtonClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    LensaTheme {
        Screen(onNextClick = {})
    }
}