package me.pablorincon.proyecto_app.ui.screens.learningpath

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.CurriculumProgress
import me.pablorincon.proyecto_app.domain.ModuleProgress
import me.pablorincon.proyecto_app.domain.ModuleStatus
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ProgressBar
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.CurrentCardBackground
import me.pablorincon.proyecto_app.ui.theme.CurrentCardBorder
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.LockedPillBackground
import me.pablorincon.proyecto_app.ui.theme.LockedPillText
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.NodeCurrentRing
import me.pablorincon.proyecto_app.ui.theme.NodeDone
import me.pablorincon.proyecto_app.ui.theme.NodeDoneRing
import me.pablorincon.proyecto_app.ui.theme.NodeLockedRing
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.TimelineLine

// Pantalla "Ruta de aprendizaje" (pestaña Aprender, guía §3), con datos reales.
@Composable
fun LearningPathScreen(
    onOpenLesson: (lessonId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearningPathViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LearningPathContent(
        state = state,
        onOpenLesson = onOpenLesson,
        onRetry = viewModel::load,
        modifier = modifier
    )
}

// Solo dibuja: avisa de lo que hace el usuario.
@Composable
private fun LearningPathContent(
    state: LearningPathUiState,
    onOpenLesson: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = AppDimens.ScreenBottom // Libre para la barra de pestañas
            )
    ) {
        when (state) {
            is LearningPathUiState.Ready -> PathBody(state.progress, onOpenLesson)
            LearningPathUiState.Loading -> {
                Header(percent = null)
                val loading = stringResource(R.string.path_screen_loading)
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Primary,
                        modifier = Modifier.semantics { contentDescription = loading }
                    )
                }
            }
            LearningPathUiState.Error -> {
                Header(percent = null)
                Spacer(Modifier.height(AppDimens.TopLineBottom))
                Text(
                    text = stringResource(R.string.path_screen_error),
                    style = AppTextStyles.SmallBody,
                    color = Muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(AppDimens.ErrorToButton))
                PrimaryButton(text = stringResource(R.string.startup_retry), onClick = onRetry)
            }
        }
    }
}

// topline: "Tu progreso" + título a la izquierda y pill con el % a la derecha.
@Composable
private fun Header(percent: Int?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.path_screen_eyebrow).uppercase(),
                style = AppTextStyles.Eyebrow,
                color = Primary
            )
            Spacer(Modifier.height(AppDimens.EyebrowBottom))
            Text(
                text = stringResource(R.string.title_learning_path),
                style = AppTextStyles.ScreenTitleSmall,
                color = Ink
            )
        }
        if (percent != null) {
            Spacer(Modifier.size(AppDimens.TopLineGap))
            Pill(
                text = stringResource(R.string.path_screen_pill, percent),
                containerColor = Mint,
                contentColor = MintInk
            )
        }
    }
}

@Composable
private fun PathBody(progress: CurriculumProgress, onOpenLesson: (String) -> Unit) {
    Header(percent = progress.percent)
    Spacer(Modifier.height(AppDimens.TitleBottom))
    Text(
        text = stringResource(R.string.path_screen_subtitle),
        style = AppTextStyles.Subtitle,
        color = Muted
    )

    Spacer(Modifier.height(AppDimens.ProgressTop))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = stringResource(R.string.path_screen_overall),
            style = AppTextStyles.RowTitle,
            color = Ink
        )
        Text(
            text = stringResource(R.string.path_screen_percent, progress.percent),
            style = AppTextStyles.RowTitle,
            color = Ink
        )
    }
    Spacer(Modifier.height(AppDimens.EyebrowBottom))
    ProgressBar(percent = progress.percent)

    Spacer(Modifier.height(AppDimens.SectionTop))
    // Línea punteada vertical de la línea de tiempo, detrás de las tarjetas
    val lineX = AppDimens.TimelineStart + AppDimens.TimelineLineX
    Column(
        verticalArrangement = Arrangement.spacedBy(AppDimens.ModuleGap),
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val x = lineX.toPx()
                drawLine(
                    color = TimelineLine,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Butt,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
                )
            }
            .padding(start = AppDimens.TimelineStart + AppDimens.TimelinePadding)
    ) {
        progress.modules.forEach { module ->
            ModuleCard(
                module = module,
                onOpen = { progress.nextLesson?.let { onOpenLesson(it.id) } }
            )
        }
    }
}

@Composable
private fun ModuleCard(module: ModuleProgress, onOpen: () -> Unit) {
    val status = module.status
    val shape = RoundedCornerShape(15.dp)
    val isCurrent = status == ModuleStatus.InProgress
    val isSoon = status == ModuleStatus.Locked && module.module.id == Curriculum.PROJECT_ID

    Box(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(if (isCurrent) CurrentCardBackground else Paper)
                .border(
                    if (isCurrent) AppDimens.CurrentCardBorderWidth else AppDimens.BorderWidth,
                    if (isCurrent) CurrentCardBorder else Line,
                    shape
                )
                .then(if (isCurrent) Modifier.clickable(onClick = onOpen) else Modifier)
                .padding(
                    start = AppDimens.ModuleCardPaddingH,
                    end = AppDimens.ModuleCardPaddingH,
                    top = AppDimens.ModuleCardPaddingTop,
                    bottom = AppDimens.ModuleCardPaddingBottom
                )
        ) {
            Text(text = stringResource(titleOf(module.module.id)), style = AppTextStyles.RowTitle.copy(fontSize = 13.sp), color = Ink)
            Spacer(Modifier.height(AppDimens.RowSubtitleTop))
            Text(text = stringResource(descriptionOf(module.module.id)), style = AppTextStyles.RowSubtitle, color = Muted)
            Spacer(Modifier.height(AppDimens.CardTextTop))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val lessons = pluralStringResource(
                    R.plurals.module_lessons, module.module.lessons.size, module.module.lessons.size
                )
                when {
                    status == ModuleStatus.Completed -> {
                        Pill(stringResource(R.string.module_status_completed), containerColor = Mint, contentColor = MintInk)
                        MetaText(lessons)
                    }
                    isCurrent -> {
                        Pill(stringResource(R.string.module_status_in_progress))
                        Text(
                            text = stringResource(R.string.module_continue),
                            style = AppTextStyles.FooterLink,
                            color = Primary
                        )
                    }
                    isSoon -> {
                        Pill(stringResource(R.string.module_status_soon), containerColor = LockedPillBackground, contentColor = LockedPillText)
                        MetaText(stringResource(R.string.module_final_project))
                    }
                    else -> {
                        Pill(stringResource(R.string.module_status_locked), containerColor = LockedPillBackground, contentColor = LockedPillText)
                        MetaText(lessons)
                    }
                }
            }
        }
        Node(
            status = status,
            modifier = Modifier
                .align(Alignment.TopStart)
                // Centrado sobre la línea: a la izquierda de la tarjeta, que empieza en TimelinePadding
                .offset(
                    x = -(AppDimens.TimelinePadding - AppDimens.TimelineLineX) - AppDimens.NodeSize / 2,
                    y = AppDimens.ModuleCardPaddingTop
                )
        )
    }
}

@Composable
private fun MetaText(text: String) {
    Text(text = text, style = AppTextStyles.RowSubtitle, color = Muted)
}

// Nodo circular de la línea de tiempo (21 dp, borde 3).
@Composable
private fun Node(status: ModuleStatus, modifier: Modifier = Modifier) {
    val (fill, ring) = when (status) {
        ModuleStatus.Completed -> NodeDone to NodeDoneRing
        ModuleStatus.InProgress -> Primary to NodeCurrentRing
        ModuleStatus.Locked -> Paper to NodeLockedRing
    }
    Box(
        modifier = modifier
            .size(AppDimens.NodeSize)
            .clip(CircleShape)
            .background(fill)
            .border(AppDimens.NodeBorder, ring, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (status == ModuleStatus.Completed) {
            Text(text = "✓", color = Color.White, style = AppTextStyles.CheckMini.copy(fontWeight = FontWeight.Black))
        }
    }
}

@StringRes
private fun titleOf(moduleId: String): Int = when (moduleId) {
    "fundamentos" -> R.string.module_fundamentos_title
    "variables" -> R.string.module_variables_title
    "condicionales" -> R.string.module_condicionales_title
    "bucles" -> R.string.module_bucles_title
    "funciones" -> R.string.module_funciones_title
    else -> R.string.module_proyecto_title
}

@StringRes
private fun descriptionOf(moduleId: String): Int = when (moduleId) {
    "fundamentos" -> R.string.module_fundamentos_desc
    "variables" -> R.string.module_variables_desc
    "condicionales" -> R.string.module_condicionales_desc
    "bucles" -> R.string.module_bucles_desc
    "funciones" -> R.string.module_funciones_desc
    else -> R.string.module_proyecto_desc
}

@Preview(showSystemUi = true)
@Composable
private fun LearningPathPreview() {
    CodeMentorTheme {
        LearningPathContent(
            state = LearningPathUiState.Ready(
                CurriculumProgress(Curriculum.Base, setOf("fundamentos_1", "fundamentos_2", "fundamentos_3", "fundamentos_4", "variables_1"))
            ),
            onOpenLesson = {},
            onRetry = {}
        )
    }
}
