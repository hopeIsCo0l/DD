package com.sook.dd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sook.dd.ui.components.EmptyState
import com.sook.dd.ui.components.PrimaryAction
import com.sook.dd.ui.theme.DDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DDTheme { DDApp() } }
    }
}

private enum class Destination(@param:StringRes val title: Int) {
    Home(R.string.home_title),
    Customers(R.string.customers_title),
    Settings(R.string.settings_title)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DDApp() {
    var destination by rememberSaveable { mutableStateOf(Destination.Home) }
    val navigateBack = { destination = Destination.Home }
    BackHandler(enabled = destination != Destination.Home, onBack = navigateBack)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(destination.title)) },
                navigationIcon = {
                    if (destination != Destination.Home) {
                        TextButton(onClick = navigateBack) {
                            Text(stringResource(R.string.action_back))
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (destination) {
                Destination.Home -> {
                    Text(
                        stringResource(R.string.home_description),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    PrimaryAction(
                        label = stringResource(R.string.customers_title),
                        onClick = { destination = Destination.Customers }
                    )
                    PrimaryAction(
                        label = stringResource(R.string.settings_title),
                        onClick = { destination = Destination.Settings }
                    )
                }
                Destination.Customers -> EmptyState(
                    title = stringResource(R.string.customers_empty_title),
                    message = stringResource(R.string.customers_empty_message)
                )
                Destination.Settings -> EmptyState(
                    title = stringResource(R.string.settings_empty_title),
                    message = stringResource(R.string.settings_empty_message)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    DDTheme { DDApp() }
}
