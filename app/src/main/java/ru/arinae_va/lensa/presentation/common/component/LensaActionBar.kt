package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.ext.toDp

@Composable
fun LensaActionBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onProfileClick: () -> Unit,
) {
    var isLogoVisible by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier.height(64.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = isLogoVisible,
            enter = fadeIn(),
            exit = fadeOut(),

            ) {
            LensaLogo(
                modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                size = LensaLogoSize.SMALL,
            )
        }

        Row(
            //modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HSpace(w = 16.dp)
            LensaIconButton(
                icon = R.drawable.ic_menu,
                iconSize = 24.dp,
                onClick = onMenuClick
            )
            HSpace(w = 24.dp)
            FSpace()
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .animateContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isLogoVisible) {
                    val focusRequester = remember {
                        FocusRequester()
                    }
                    LensaInput(
                        modifier = Modifier.width(290.dp),
                        focusRequester = focusRequester,
                        onValueChanged = onSearchTextChanged,
                        value = searchQuery,
                        placeholder = "Поиск по специалистам",
                        showLeadingIcon = true,
                        leadingIconRes = R.drawable.ic_loupe,
                        onLeadingIconClick = { isLogoVisible = !isLogoVisible },
                        onFocusChanged = { isFocused ->
                            isLogoVisible = !isFocused
                        },
                        isRoundedShape = true,
                        isFillMaxWidth = false,
                    )
                    LaunchedEffect(key1 = focusRequester) {
                        focusRequester.requestFocus()
                    }
                } else {
                    LensaIconButton(
                        icon = R.drawable.ic_loupe,
                        iconSize = 24.dp,
                        onClick = {
                            isLogoVisible = !isLogoVisible
                        },
                    )
                }
            }
            HSpace(w = 16.dp)
            LensaIconButton(
                icon = R.drawable.ic_profile,
                iconSize = 24.dp,
                onClick = onProfileClick
            )
            HSpace(w = 16.dp)

        }
    }

}