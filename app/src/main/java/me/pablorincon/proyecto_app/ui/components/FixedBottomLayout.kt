package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens

// Pantalla con contenido desplazable y una zona fija abajo (.bottom-fixed del mockup):
// el botón principal queda siempre visible y un degradado difumina el contenido
// que pasa por debajo. El final del contenido deja libre el alto de esa zona.
@Composable
fun FixedBottomLayout(
    bottomBar: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var bottomBarHeight by remember { mutableIntStateOf(0) }
    val bottomBarHeightDp = with(LocalDensity.current) { bottomBarHeight.toDp() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(
                    start = AppDimens.ScreenHorizontal,
                    end = AppDimens.ScreenHorizontal,
                    top = AppDimens.ScreenTop,
                    bottom = bottomBarHeightDp
                ),
            content = content
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .onSizeChanged { bottomBarHeight = it.height }
                .drawBehind {
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, AppBackground),
                            startY = 0f,
                            endY = AppDimens.BottomFade.toPx()
                        )
                    )
                }
                .padding(
                    start = AppDimens.ScreenHorizontal,
                    end = AppDimens.ScreenHorizontal,
                    top = AppDimens.BottomFixedTop
                )
                .navigationBarsPadding()
                .padding(bottom = AppDimens.BottomFixed),
            content = bottomBar
        )
    }
}
