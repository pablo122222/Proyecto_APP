package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.ProgressFill
import me.pablorincon.proyecto_app.ui.theme.ProgressTrack

// Barra de progreso (guía §0): alto 7, pista gris y relleno con degradado azul → verde.
// `fill` permite otro color (la vida del jefe va en rojo).
@Composable
fun ProgressBar(percent: Int, modifier: Modifier = Modifier, fill: Brush = ProgressFill) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimens.ProgressBarHeight)
            .clip(AppShapes.Pill)
            .background(ProgressTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(percent.coerceIn(0, 100) / 100f)
                .background(fill)
        )
    }
}
