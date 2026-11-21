package com.sook.dd.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sook.dd.R
import com.sook.dd.ui.theme.DDTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        isError = error != null,
        supportingText = error?.let { message -> { ErrorText(message) } }
    )
}

@Composable
fun PrimaryAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled && !loading
    ) {
        if (loading) LoadingState(stringResource(R.string.status_saving))
        else Text(label)
    }
}

@Composable
fun ErrorText(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun EmptyState(title: String, message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LoadingState(
    message: String = stringResource(R.string.status_loading),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        Text(message, style = MaterialTheme.typography.bodyMedium)
    }
}

enum class SaveStatus { Idle, Saving, Saved, Error }

/** The caller owns the operation and resets status when the form changes. */
@Composable
fun SaveStatusPresentation(
    status: SaveStatus,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    when (status) {
        SaveStatus.Idle -> Unit
        SaveStatus.Saving -> LoadingState(stringResource(R.string.status_saving), modifier)
        SaveStatus.Saved -> Text(
            text = stringResource(R.string.status_saved),
            modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        SaveStatus.Error -> ErrorText(
            errorMessage ?: stringResource(R.string.status_save_error),
            modifier
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CommonComponentsPreview() {
    DDTheme(dynamicColor = false) {
        Surface {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppTextField(
                    value = "",
                    onValueChange = {},
                    label = stringResource(R.string.customer_name),
                    error = stringResource(R.string.error_required)
                )
                PrimaryAction(stringResource(R.string.action_save), onClick = {})
                LoadingState()
                SaveStatusPresentation(SaveStatus.Saving)
                SaveStatusPresentation(SaveStatus.Saved)
                SaveStatusPresentation(SaveStatus.Error)
                EmptyState(
                    stringResource(R.string.customers_empty_title),
                    stringResource(R.string.customers_empty_message)
                )
            }
        }
    }
}
