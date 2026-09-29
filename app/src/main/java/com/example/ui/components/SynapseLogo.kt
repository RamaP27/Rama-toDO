package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.theme.Primary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.Tertiary

const val LOGO_URL = "https://lh3.googleusercontent.com/aida/AEtjO1V6l6aidmZp7S8OT9KRgOON7nJpV-O26UV7GWa_T6aHhHdsT8Sezl-zyHTtaW26I0VDYeMYMU8LlptMkw5NPyZEgGXK4sKX_vhEyAc6180eg0KwQbBPxB4xibZXivxv59e0t2yjXc_YwVa_hFRCuONRFXuXnW3TSYyTACSlo3x66TvvGpzCeWgtHmH9X_4lY32uPRTK7wL2jvjMV5G7b9LwA8D65o3iKM3JKaINpW5DlxXosZqvNgJnMg"
const val AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuAF52LOoa8hPLEoAVGJimI_bTRdIK-mOfT6KtvX23kZmQ4s69sqcKFy8ryI3sXcVdZDN0tKiZYt_AyHF_rPnPGTcmCProD7GD-3Ror1NS2DucmdbnxzMyRnj9eI4zvaG-M5ITEj7XpPsMXeiM7lBYpPUv9NNK8zbr2tnDPR1TcMLFU_yzYlJvVZfAFgUriDDG2bwu-Glomi2b8ktzB6hrG744fddOrzyd9cd-I0ReuVhOUGoz-g9YE"

@Composable
fun SynapseLogo(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF2E1C59), Color(0xFF1B1838))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Synapse Logo",
            tint = Primary,
            modifier = Modifier.size(size * 0.7f)
        )
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Tertiary,
            modifier = Modifier
                .size(size * 0.35f)
                .align(Alignment.TopEnd)
        )
    }
}
