package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LensaDropdownInput(
    inputType: KeyboardType = KeyboardType.Text,
    value: String = "",
    modifier: Modifier = Modifier,
    allowFreeInput: Boolean = false,
    showRequired: Boolean = false,
    items: List<String>,
    placeholder: String = "",
    onValueChanged: (String) -> Unit,
) {
    // TODO соответствие дизайну
    var expanded by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf(value) }
    Box {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
        ) {
            LensaInput(
                modifier = modifier,
                inputType = inputType,
                showRequired = showRequired,
                placeholder = placeholder,
                onValueChanged = { new ->
                    if (allowFreeInput) {
                        input = new
                        onValueChanged.invoke(new)
                    }
                },
                readOnly = !allowFreeInput,
                value = input,
                onFocusChanged = { isFocused ->
                    expanded = isFocused
                }
            )
            val filteredItems =
                if (allowFreeInput && input.isNotBlank()) items.filter { it.contains(input) }
                else items
            if (filteredItems.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        if (!allowFreeInput && !items.contains(input)) {
                            input = ""
                        }
                    }
                ) {
                    filteredItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                //selectedText = item
                                expanded = false
                                input = item
                                onValueChanged.invoke(item)
                                //Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}