package me.pablorincon.proyecto_app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Chevron
import me.pablorincon.proyecto_app.ui.theme.ErrorRed
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.SettingsBand
import me.pablorincon.proyecto_app.ui.theme.SwitchOff
import me.pablorincon.proyecto_app.ui.theme.motionMillis

// Componentes de las listas de ajustes (Perfil, Configuración y sus pantallas internas):
// filas a ancho completo sobre fondo blanco, icono de línea, encabezados grises por sección
// y franjas grises entre secciones (referencia visual dada por el dueño del proyecto).

private val RowPaddingH = 22.dp
private val IconSize = 24.dp
private val IconGap = 16.dp

// Pantalla interna con barra superior (atrás + título) y contenido desplazable en blanco.
@Composable
fun SettingsScaffold(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(14.dp))
            Text(
                text = title,
                style = AppTextStyles.SubScreenTitle,
                color = Ink,
                modifier = Modifier.semantics { heading() }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            content = content
        )
    }
}

// Encabezado gris de una sección ("Tu cuenta", "Accesibilidad"...).
@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = AppTextStyles.SettingsHeader,
        color = Muted,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { heading() }
            .padding(start = RowPaddingH, end = RowPaddingH, top = 18.dp, bottom = 4.dp)
    )
}

// Franja gris que separa secciones.
@Composable
fun SettingsBandDivider() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(8.dp)
            .background(SettingsBand)
    )
}

// Fila con icono, título, subtítulo opcional, valor actual a la derecha y chevron.
@Composable
fun SettingsRow(
    @DrawableRes icon: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    value: String? = null,
    danger: Boolean = false,
    showChevron: Boolean = true,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = RowPaddingH, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIcon(icon, danger)
        Spacer(Modifier.width(IconGap))
        RowTexts(title, subtitle, danger, Modifier.weight(1f))
        if (value != null) {
            Spacer(Modifier.width(8.dp))
            // El valor nunca ocupa más que el título (importa con el texto grande)
            Text(
                text = value,
                style = AppTextStyles.SettingsValue,
                color = Muted,
                textAlign = TextAlign.End,
                maxLines = 2,
                modifier = Modifier.widthIn(max = 150.dp)
            )
        }
        if (showChevron && !danger) {
            Spacer(Modifier.width(8.dp))
            Text(text = "›", style = AppTextStyles.RowChevron, color = Chevron, modifier = Modifier.decorative())
        }
    }
}

// Fila con interruptor (toda la fila lo cambia, como en los ajustes del sistema).
@Composable
fun SettingsToggleRow(
    @DrawableRes icon: Int,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange)
            .padding(horizontal = RowPaddingH, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIcon(icon, danger = false)
        Spacer(Modifier.width(IconGap))
        RowTexts(title, subtitle, danger = false, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        AppSwitch(checked = checked)
    }
}

// Nota pequeña bajo una sección.
@Composable
fun SettingsNote(text: String) {
    Text(
        text = text,
        style = AppTextStyles.SettingsNote,
        color = Muted,
        modifier = Modifier.padding(start = RowPaddingH, end = RowPaddingH, top = 2.dp, bottom = 4.dp)
    )
}

@Composable
private fun RowIcon(@DrawableRes icon: Int, danger: Boolean) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null, // El título de la fila ya lo describe
        tint = if (danger) ErrorRed else Ink,
        modifier = Modifier.size(IconSize)
    )
}

@Composable
private fun RowTexts(title: String, subtitle: String?, danger: Boolean, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.Center) {
        Text(text = title, style = AppTextStyles.SettingsTitle, color = if (danger) ErrorRed else Ink)
        if (subtitle != null) {
            Spacer(Modifier.height(2.dp))
            Text(text = subtitle, style = AppTextStyles.SettingsSubtitle, color = Muted)
        }
    }
}

// Interruptor de la guía §8: 40×23, apagado gris, encendido azul, perilla blanca de 17 dp
// que se desplaza 17 dp. Solo dibuja: la fila que lo contiene maneja el toque.
@Composable
fun AppSwitch(checked: Boolean, modifier: Modifier = Modifier) {
    val knobOffset by animateDpAsState(if (checked) 20.dp else 3.dp, tween(motionMillis(150)), label = "switchKnob")
    val track by animateColorAsState(if (checked) Primary else SwitchOff, tween(motionMillis(150)), label = "switchTrack")
    Box(
        modifier = modifier
            .size(width = 40.dp, height = 23.dp)
            .clip(CircleShape)
            .background(track)
    ) {
        Box(
            Modifier
                .offset { IntOffset(knobOffset.roundToPx(), 3.dp.roundToPx()) }
                .size(17.dp)
                .clip(CircleShape)
                .background(Paper)
        )
    }
}
