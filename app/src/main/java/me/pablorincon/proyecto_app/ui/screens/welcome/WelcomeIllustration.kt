package me.pablorincon.proyecto_app.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.ui.theme.AccentYellow
import me.pablorincon.proyecto_app.ui.theme.AiViolet
import me.pablorincon.proyecto_app.ui.theme.AiVioletSoft
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.FloatCardBorder
import me.pablorincon.proyecto_app.ui.theme.FloatCardShadow
import me.pablorincon.proyecto_app.ui.theme.FloatCardText
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.OrbShadow
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// Ilustración de la Bienvenida (.welcome-art): fondo degradado, cubo {;} girado,
// dos tarjetitas flotantes y un punto amarillo.
@Composable
fun WelcomeIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimens.IllustrationHeight)
            .clip(AppShapes.Illustration)
            .background(cssLinearGradient(145f, listOf(AiVioletSoft, Mint))),
        contentAlignment = Alignment.Center
    ) {
        // Cubo central {;}
        Box(
            modifier = Modifier
                .size(AppDimens.OrbSize)
                .rotate(AppDimens.OrbRotation)
                .dropShadow(
                    shape = AppShapes.Orb,
                    shadow = Shadow(radius = 28.dp, color = OrbShadow, offset = DpOffset(0.dp, 18.dp))
                )
                .clip(AppShapes.Orb)
                .background(cssLinearGradient(145f, listOf(AiViolet, Primary))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_orb_symbol),
                style = AppTextStyles.OrbSymbol,
                color = Color.White
            )
        }

        // Tarjetita de código, arriba a la izquierda
        FloatCard(
            text = stringResource(R.string.welcome_code_card),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = AppDimens.FloatCardOneStart, top = AppDimens.FloatCardInset)
                .rotate(-AppDimens.FloatCardRotation)
        )

        // Tarjetita "✓ reto completado", abajo a la derecha
        FloatCard(
            text = stringResource(R.string.welcome_done_card),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = AppDimens.FloatCardTwoEnd, bottom = AppDimens.FloatCardInset)
                .rotate(AppDimens.FloatCardRotation)
        )

        // Punto amarillo decorativo
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = AppDimens.FloatDotEnd, top = AppDimens.FloatDotTop)
                .size(AppDimens.FloatDotSize)
                .background(AccentYellow, CircleShape)
        )
    }
}

// Tarjetita blanca con texto monoespaciado (.float-card).
@Composable
private fun FloatCard(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = AppTextStyles.CodeCard,
        color = FloatCardText,
        modifier = modifier
            .dropShadow(
                shape = AppShapes.FloatCard,
                shadow = Shadow(radius = 24.dp, color = FloatCardShadow, offset = DpOffset(0.dp, 10.dp))
            )
            .clip(AppShapes.FloatCard)
            .background(Paper)
            .border(AppDimens.BorderWidth, FloatCardBorder, AppShapes.FloatCard)
            .padding(horizontal = AppDimens.FloatCardPaddingH, vertical = AppDimens.FloatCardPaddingV)
    )
}

// Degradado lineal con ángulo al estilo CSS (`linear-gradient(145deg, ...)`):
// 0° apunta hacia arriba y el ángulo gira en sentido horario, igual que en el navegador.
private fun cssLinearGradient(angleDegrees: Float, colors: List<Color>): Brush =
    object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val radians = Math.toRadians(angleDegrees.toDouble())
            val dx = sin(radians).toFloat()
            val dy = -cos(radians).toFloat()
            // Largo de la línea del degradado según la especificación de CSS
            val half = (abs(size.width * dx) + abs(size.height * dy)) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            return LinearGradientShader(
                from = Offset(center.x - dx * half, center.y - dy * half),
                to = Offset(center.x + dx * half, center.y + dy * half),
                colors = colors
            )
        }
    }
