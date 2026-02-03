package vegabobo.dsusideloader.ui.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vegabobo.dsusideloader.R
import vegabobo.dsusideloader.ui.components.CardBox
import vegabobo.dsusideloader.ui.components.FileSelectionBox
import vegabobo.dsusideloader.ui.components.PreferenceItem
import vegabobo.dsusideloader.ui.screen.home.UserDataCardState

@Composable
fun UserdataCard(
    isEnabled: Boolean,
    uiState: UserDataCardState,
    isDsuInstalled: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit = {},
    onPreserveCheckedChange: (Boolean) -> Unit = {},
) {
    // When DSU is installed and not currently installing, show simplified preserve UI
    // Note: isEnabled parameter indicates installation is in progress (when true)
    // So !isEnabled means installation is NOT in progress
    if (isDsuInstalled && !isEnabled) {
        // When DSU is installed, show a simpler card with preserve option
        CardBox(
            modifier = modifier,
            cardTitle = stringResource(id = R.string.userdata_size),
            addToggle = false,
        ) {
            Column {
                // Preserve userdata toggle (default: ON)
                PreferenceItem(
                    title = stringResource(id = R.string.preserve_userdata),
                    description = stringResource(id = R.string.preserve_userdata_desc),
                    showToggle = true,
                    isChecked = uiState.preserveSelected,
                    isEnabled = !isEnabled,
                    onClick = { onPreserveCheckedChange(!uiState.preserveSelected) }
                )
                
                // Show userdata size input only when preserve is OFF
                AnimatedVisibility(visible = !uiState.preserveSelected) {
                    Column {
                        FileSelectionBox(
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                            isEnabled = !isEnabled,
                            isError = uiState.isError,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textFieldValue = uiState.text,
                            textFieldTitle = stringResource(id = R.string.userdata_size_info),
                            onValueChange = onValueChange,
                        )
                        AnimatedVisibility(visible = uiState.isError) {
                            Text(
                                modifier = Modifier.padding(start = 1.dp),
                                text = stringResource(
                                    id = R.string.allowed_userdata_allocation,
                                    uiState.maximumAllowed,
                                ),
                                color = MaterialTheme.colorScheme.error,
                                lineHeight = 14.sp,
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Normal userdata card when DSU is not installed
        CardBox(
            modifier = modifier,
            cardTitle = stringResource(id = R.string.userdata_size),
            addToggle = true,
            isToggleEnabled = !isEnabled,
            isToggleChecked = uiState.isSelected,
            onCheckedChange = onCheckedChange,
        ) {
            AnimatedVisibility(
                visible = uiState.isSelected,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column {
                    FileSelectionBox(
                        modifier = Modifier.padding(bottom = 4.dp),
                        isEnabled = !isEnabled,
                        isError = uiState.isError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textFieldValue = uiState.text,
                        textFieldTitle = stringResource(id = R.string.userdata_size_info),
                        onValueChange = onValueChange,
                    )
                    AnimatedVisibility(visible = uiState.isError) {
                        Text(
                            modifier = Modifier.padding(start = 1.dp),
                            text = stringResource(
                                id = R.string.allowed_userdata_allocation,
                                uiState.maximumAllowed,
                            ),
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 14.sp,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }
    }
}
