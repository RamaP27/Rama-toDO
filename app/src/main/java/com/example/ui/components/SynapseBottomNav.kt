package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SurfaceContainer

enum class SynapseScreen(val title: String, val icon: ImageVector) {
    TODAY("Today", Icons.Default.CheckCircle),
    BREAKDOWN("Breakdown", Icons.Default.AccountTree),
    INSIGHTS("Insights", Icons.Default.BarChart),
    SETTINGS("Settings", Icons.Default.Tune)
}

@Composable
fun SynapseBottomNav(
    currentScreen: SynapseScreen,
    onScreenSelected: (SynapseScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceContainer.copy(alpha = 0.95f),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SynapseScreen.values().forEach { screen ->
                val isSelected = currentScreen == screen
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onScreenSelected(screen) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("nav_tab_${screen.name.lowercase()}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) SecondaryContainer else androidx.compose.ui.graphics.Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = if (isSelected) OnSecondaryContainer else OnSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isSelected) OnSurface else OnSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
