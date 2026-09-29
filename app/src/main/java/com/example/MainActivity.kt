package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.SynapseBottomNav
import com.example.ui.components.SynapseScreen
import com.example.ui.screens.BreakdownScreen
import com.example.ui.screens.CopilotSessionScreen
import com.example.ui.screens.InsightsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Surface
import com.example.ui.viewmodel.CopilotViewModel
import com.example.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SynapseApp()
            }
        }
    }
}

@Composable
fun SynapseApp() {
    val taskViewModel: TaskViewModel = viewModel()
    val copilotViewModel: CopilotViewModel = viewModel()

    var currentScreen by remember { mutableStateOf(SynapseScreen.TODAY) }
    var isCopilotOpen by remember { mutableStateOf(false) }

    if (isCopilotOpen) {
        CopilotSessionScreen(
            viewModel = copilotViewModel,
            onNavigateBack = { isCopilotOpen = false }
        )
    } else {
        Scaffold(
            bottomBar = {
                SynapseBottomNav(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it }
                )
            },
            containerColor = Surface,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                when (currentScreen) {
                    SynapseScreen.TODAY -> TodayScreen(
                        viewModel = taskViewModel,
                        onNavigateToCopilot = { isCopilotOpen = true }
                    )
                    SynapseScreen.BREAKDOWN -> BreakdownScreen(
                        viewModel = taskViewModel,
                        onNavigateToCopilot = { isCopilotOpen = true }
                    )
                    SynapseScreen.INSIGHTS -> InsightsScreen(
                        viewModel = taskViewModel,
                        onNavigateToCopilot = { isCopilotOpen = true }
                    )
                    SynapseScreen.SETTINGS -> SettingsScreen(
                        viewModel = taskViewModel,
                        onNavigateToCopilot = { isCopilotOpen = true }
                    )
                }
            }
        }
    }
}
