package me.pablorincon.proyecto_app.ui.screens.learningpath

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.Boss
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.CurriculumProgress
import me.pablorincon.proyecto_app.domain.ModuleProgress
import me.pablorincon.proyecto_app.domain.ModuleStatus
import me.pablorincon.proyecto_app.domain.PathStep
import me.pablorincon.proyecto_app.domain.StepStatus
import me.pablorincon.proyecto_app.ui.components.MoreSoonCard
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ProgressBar
import me.pablorincon.proyecto_app.ui.theme.AccentYellow
import me.pablorincon.proyecto_app.ui.theme.AiViolet
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.HeroEnd
import me.pablorincon.proyecto_app.ui.theme.HeroStart
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.NodeCurrentRing
import me.pablorincon.proyecto_app.ui.theme.NodeDone
import me.pablorincon.proyecto_app.ui.theme.NodeDoneShadow
import me.pablorincon.proyecto_app.ui.theme.NodeLockedFill
import me.pablorincon.proyecto_app.ui.theme.NodeLockedShadow
import me.pablorincon.proyecto_app.ui.theme.NodeLockedText
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft

// Pantalla "Ruta de aprendizaje" (pestaña Aprender): el camino de nodos en zigzag, nivel por
// nivel. Cada nivel tiene 11 lecciones y su jefe final; el jefe hay que vencerlo para pasar.
@Composable
fun LearningPathScreen(
    onOpenStep: (PathStep) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearningPathViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    // Al volver a la Ruta (de una lección o de otra pestaña) se actualiza el progreso
    LifecycleResumeEffect(viewModel) {
        viewModel.load(silent = true)
        onPauseOrDispose { }
    }
    LearningPathContent(
        state = state,
        onOpenStep = onOpenStep,
        onRetry = viewModel::load,
        modifier = modifier
    )
}

// Solo dibuja: avisa de lo que hace el usuario.
@Composable
private fun LearningPathContent(
    state: LearningPathUiState,
    onOpenStep: (PathStep) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is LearningPathUiState.Ready -> PathList(state.progress, onOpenStep, modifier)
        LearningPathUiState.Loading, LearningPathUiState.Error -> Column(
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
            Header(percent = null)
            if (state == LearningPathUiState.Loading) {
                val loading = stringResource(R.string.path_screen_loading)
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Primary,
                        modifier = Modifier.semantics { contentDescription = loading }
                    )
                }
            } else {
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

// Zigzag de los nodos: desplazamiento horizontal (dp) de cada paso, que se repite cada 8.
private val ZigzagOffsets = listOf(0, 34, 52, 34, 0, -34, -52, -34)
private val LessonNodeSize = 64.dp
private val BossNodeSize = 80.dp
private val NodeDepth = 5.dp   // grosor del "borde de abajo" del nodo
private val NodeHalo = 12.dp   // espacio extra para el anillo del paso actual

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PathList(
    progress: CurriculumProgress,
    onOpenStep: (PathStep) -> Unit,
    modifier: Modifier = Modifier
) {
    val titles = stringArrayResource(R.array.level_titles)
    val descriptions = stringArrayResource(R.array.level_descriptions)
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val lockedMessage = stringResource(R.string.path_locked_toast)

    // Al abrir la Ruta se baja al paso actual (una sola vez, al primer dibujo con datos)
    val target = remember { currentItemIndex(progress) }
    LaunchedEffect(Unit) { listState.scrollToItem(target) }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            start = AppDimens.ScreenHorizontal,
            end = AppDimens.ScreenHorizontal,
            top = AppDimens.ScreenTop,
            bottom = AppDimens.ScreenBottom // Libre para la barra de pestañas
        )
    ) {
        item(key = "header") {
            Header(percent = progress.percent)
            Spacer(Modifier.height(AppDimens.TitleBottom))
            Text(
                text = stringResource(R.string.path_screen_subtitle),
                style = AppTextStyles.Subtitle,
                color = Muted
            )
            Spacer(Modifier.height(AppDimens.ProgressTop))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.path_screen_overall), style = AppTextStyles.RowTitle, color = Ink)
                Text(
                    stringResource(R.string.path_screen_percent, progress.percent),
                    style = AppTextStyles.RowTitle,
                    color = Ink
                )
            }
            Spacer(Modifier.height(AppDimens.EyebrowBottom))
            ProgressBar(percent = progress.percent)
            Spacer(Modifier.height(AppDimens.EyebrowBottom))
        }

        progress.modules.forEachIndexed { levelIndex, module ->
            // La barra del nivel se queda fija arriba mientras se recorren sus nodos
            stickyHeader(key = "level_${module.module.id}") {
                LevelBanner(
                    module = module,
                    level = levelIndex + 1,
                    title = titles.getOrElse(levelIndex) { "" },
                    description = descriptions.getOrElse(levelIndex) { "" }
                )
            }
            stepNodes(module, progress, onOpenStep) {
                Toast.makeText(context, lockedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        item(key = "soon") {
            Spacer(Modifier.height(AppDimens.SectionTop))
            MoreSoonCard()
        }
    }
}

// Índice (dentro de la lista) del paso actual, para desplazarse a él al abrir.
// Orden de la lista: cabecera, y por cada nivel su barra + sus 12 nodos.
private fun currentItemIndex(progress: CurriculumProgress): Int {
    val next = progress.nextStep ?: return 0
    var index = 1 // cabecera
    for (module in progress.modules) {
        index += 1 // barra del nivel
        val position = module.module.steps.indexOfFirst { it.id == next.id }
        if (position >= 0) return (index + position - 2).coerceAtLeast(0) // un poco antes, para ver contexto
        index += module.module.steps.size
    }
    return 0
}

private fun LazyListScope.stepNodes(
    module: ModuleProgress,
    progress: CurriculumProgress,
    onOpenStep: (PathStep) -> Unit,
    onLocked: () -> Unit
) {
    module.module.steps.forEachIndexed { stepIndex, step ->
        item(key = step.id) {
            val status = progress.stepStatus(step.id)
            PathNode(
                step = step,
                number = stepIndex + 1,
                status = status,
                horizontalOffset = ZigzagOffsets[stepIndex % ZigzagOffsets.size].dp,
                onClick = { if (status == StepStatus.Locked) onLocked() else onOpenStep(step) }
            )
        }
    }
}

// Dificultad máxima que se dibuja con estrellas (la dificultad sube con el nivel).
private const val MAX_STARS = 5

// Barra del nivel: "NIVEL 3 ★★★☆☆", su nombre, su descripción y cuántos pasos lleva.
@Composable
private fun LevelBanner(module: ModuleProgress, level: Int, title: String, description: String) {
    val shape = RoundedCornerShape(15.dp)
    val brush: Brush
    val content: Color
    val soft: Color
    when (module.status) {
        ModuleStatus.InProgress -> {
            brush = Brush.horizontalGradient(listOf(HeroStart, HeroEnd)); content = Color.White; soft = Color.White.copy(alpha = 0.85f)
        }
        ModuleStatus.Completed -> {
            brush = Brush.horizontalGradient(listOf(NodeDone, NodeDone)); content = Color.White; soft = Color.White.copy(alpha = 0.9f)
        }
        ModuleStatus.Optional -> {
            brush = Brush.horizontalGradient(listOf(PrimarySoft, PrimarySoft)); content = PrimaryDark; soft = Muted
        }
        ModuleStatus.Locked -> {
            brush = Brush.horizontalGradient(listOf(NodeLockedFill, NodeLockedFill)); content = NodeLockedText; soft = NodeLockedText
        }
    }
    val stars = level.coerceAtMost(MAX_STARS)
    val starsDescription = stringResource(R.string.module_level_description, level, stars, MAX_STARS)
    val levelLabel = stringResource(R.string.module_level, level).uppercase()
    // El fondo opaco evita que los nodos se vean pasar por detrás de la barra fija
    Box(Modifier.fillMaxWidth().background(AppBackground).padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(brush)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        append(levelLabel)
                        append("  ")
                        withStyle(SpanStyle(color = AccentYellow)) { append("★".repeat(stars)) }
                        withStyle(SpanStyle(color = content.copy(alpha = 0.3f))) { append("★".repeat(MAX_STARS - stars)) }
                    },
                    style = AppTextStyles.Eyebrow,
                    color = content,
                    modifier = Modifier.semantics { contentDescription = starsDescription }
                )
                Spacer(Modifier.height(3.dp))
                Text(text = title, style = AppTextStyles.SectionTitle.copy(fontSize = 15.sp), color = content)
                Spacer(Modifier.height(2.dp))
                Text(text = description, style = AppTextStyles.RowSubtitle, color = soft)
            }
            Spacer(Modifier.size(10.dp))
            Text(
                text = stringResource(R.string.level_progress, module.completedSteps, module.module.steps.size),
                style = AppTextStyles.Pill,
                color = content,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(content.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}

// Un nodo del camino. Lección: círculo con su número. Jefe: círculo grande con 👾.
// Hecho: verde con ✓ · Siguiente: azul con "Empezar" · Disponible (repaso): borde azul · Bloqueado: gris.
@Composable
private fun PathNode(
    step: PathStep,
    number: Int,
    status: StepStatus,
    horizontalOffset: Dp,
    onClick: () -> Unit
) {
    val isBoss = step is Boss
    val size = if (isBoss) BossNodeSize else LessonNodeSize
    val accent = if (isBoss) AiViolet else Primary
    val accentShadow = if (isBoss) HeroEnd else PrimaryDark

    val fill: Color
    val shadow: Color
    val ring: Color?
    when (status) {
        StepStatus.Done -> { fill = NodeDone; shadow = NodeDoneShadow; ring = null }
        StepStatus.Current -> { fill = accent; shadow = accentShadow; ring = if (isBoss) AiViolet.copy(alpha = 0.25f) else NodeCurrentRing }
        StepStatus.Available -> { fill = Paper; shadow = accent.copy(alpha = 0.35f); ring = null }
        StepStatus.Locked -> { fill = NodeLockedFill; shadow = NodeLockedShadow; ring = null }
    }
    val symbolColor = when (status) {
        StepStatus.Done, StepStatus.Current -> Color.White
        StepStatus.Available -> accent
        StepStatus.Locked -> NodeLockedText
    }

    val stateText = stringResource(
        when (status) {
            StepStatus.Done -> R.string.node_state_done
            StepStatus.Current -> R.string.node_state_current
            StepStatus.Available -> R.string.node_state_available
            StepStatus.Locked -> R.string.node_state_locked
        }
    )
    val name = if (isBoss) stringResource(R.string.node_boss_description)
    else stringResource(R.string.node_lesson_description, number)
    val description = "$name, $stateText"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (status == StepStatus.Current) {
            Pill(
                text = stringResource(R.string.node_start).uppercase(),
                containerColor = Paper,
                contentColor = accent,
                modifier = Modifier
                    .offset(x = horizontalOffset)
                    .border(AppDimens.BorderWidth, accent, CircleShape)
            )
            Spacer(Modifier.height(6.dp))
        }
        Box(
            modifier = Modifier
                .offset(x = horizontalOffset)
                .size(size + NodeHalo)
                .semantics(mergeDescendants = true) {
                    contentDescription = description
                    role = Role.Button
                }
                .clickable(
                    onClick = onClick,
                    indication = null, // El nodo ya cambia de color: sin ripple
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (ring != null) {
                Box(Modifier.size(size + NodeHalo).clip(CircleShape).background(ring))
            }
            // Borde de abajo (da la sensación de botón con relieve)
            Box(Modifier.offset(y = NodeDepth / 2).size(size).clip(CircleShape).background(shadow))
            Box(
                modifier = Modifier
                    .offset(y = -NodeDepth / 2)
                    .size(size)
                    .clip(CircleShape)
                    .background(fill)
                    .then(
                        if (status == StepStatus.Available) Modifier.border(3.dp, accent, CircleShape) else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    status == StepStatus.Done -> Text("✓", color = symbolColor, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    isBoss -> Text("👾", fontSize = 32.sp, modifier = Modifier.alpha(if (status == StepStatus.Locked) 0.5f else 1f))
                    else -> Text(number.toString(), color = symbolColor, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LearningPathPreview() {
    CodeMentorTheme {
        LearningPathContent(
            state = LearningPathUiState.Ready(
                CurriculumProgress(
                    Curriculum.Base,
                    (1..5).map { Curriculum.lessonId(1, it) }.toSet()
                )
            ),
            onOpenStep = {},
            onRetry = {}
        )
    }
}
