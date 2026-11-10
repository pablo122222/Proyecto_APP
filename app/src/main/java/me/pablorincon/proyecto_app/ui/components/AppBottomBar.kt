package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import me.pablorincon.proyecto_app.ui.navigation.BottomTab
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft
import me.pablorincon.proyecto_app.ui.theme.TabBarBackground
import me.pablorincon.proyecto_app.ui.theme.TabInactive

// Barra de pestañas inferior (.tabbar): alto 73, blanco al 93 % y borde superior.
// El mockup además desenfoca lo que pasa por detrás; Android solo lo permite
// desde la API 31 (minSdk es 28), así que se deja sin desenfoque.
// El fondo se extiende bajo la barra de navegación del sistema.
@Composable
fun AppBottomBar(
    activeTab: BottomTab,
    onTabClick: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(TabBarBackground)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(AppDimens.BorderWidth)
                .background(Line)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.TabBarHeight)
                .selectableGroup() // TalkBack anuncia "pestaña X de 5"
        ) {
            BottomTab.entries.forEach { tab ->
                TabItem(
                    tab = tab,
                    selected = tab == activeTab,
                    onClick = { onTabClick(tab) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

// Un botón de la barra: icono dentro de un cuadro 26×26 y el nombre debajo.
@Composable
private fun TabItem(
    tab: BottomTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) Primary else TabInactive
    Column(
        modifier = modifier
            .fillMaxHeight()
            .selectable(selected = selected, role = Role.Tab, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppDimens.TabLabelGap, Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.TabIconBox)
                .clip(AppShapes.TabIcon)
                .background(if (selected) PrimarySoft else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(tab.icon),
                contentDescription = null, // El texto de abajo ya nombra la pestaña
                tint = color,
                modifier = Modifier.size(AppDimens.TabIcon)
            )
        }
        Text(text = stringResource(tab.label), style = tabLabelStyle(), color = color, maxLines = 1)
    }
}

// La barra tiene alto fijo y cinco nombres en fila: el nombre crece con la letra del sistema
// solo hasta 1,5 veces; más allá se tocaban ("AprenderRetos") y se salían de la barra.
@Composable
private fun tabLabelStyle(): TextStyle {
    val base = AppTextStyles.TabLabel
    val scale = LocalDensity.current.fontScale
    return if (scale <= MAX_TAB_LABEL_SCALE) base else base.copy(fontSize = base.fontSize * (MAX_TAB_LABEL_SCALE / scale))
}

private const val MAX_TAB_LABEL_SCALE = 1.5f

@Preview
@Composable
private fun AppBottomBarPreview() {
    CodeMentorTheme {
        AppBottomBar(activeTab = BottomTab.Home, onTabClick = {})
    }
}
