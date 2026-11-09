package me.pablorincon.proyecto_app.ui.screens.boss

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.Boss
import me.pablorincon.proyecto_app.domain.BossContent
import me.pablorincon.proyecto_app.domain.LessonQuiz
import me.pablorincon.proyecto_app.domain.MultipleChoice
import me.pablorincon.proyecto_app.domain.PathStep
import me.pablorincon.proyecto_app.ui.components.BackButton
import me.pablorincon.proyecto_app.ui.components.MoreSoonCard
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ProgressBar
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.screens.lesson.ExerciseCard
import me.pablorincon.proyecto_app.ui.screens.lesson.SaveState
import me.pablorincon.proyecto_app.ui.theme.AccentYellow
import me.pablorincon.proyecto_app.ui.theme.AiViolet
import me.pablorincon.proyecto_app.ui.theme.AiVioletSoft
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.ErrorRed
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadText
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary

// Pantalla del jefe de un nivel: una batalla de ejercicios con barra de vida. Se baja su vida
// resolviendo ejercicios; al vencerlo se desbloquea el siguiente nivel.
@Composable
fun BossScreen(
    bossId: String,
    onBack: () -> Unit,
    onNextStep: (PathStep) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BossViewModel = viewModel(
        key = bossId,
        factory = viewModelFactory { initializer { BossViewModel(bossId) } }
    )
    val state by viewModel.uiState.collectAsState()
    BossContentView(
        state = state,
        onBack = onBack,
        onRetryLoad = viewModel::load,
        onSubmit = viewModel::onSubmit,
        onNext = viewModel::onNext,
        onRetrySave = viewModel::onRetrySave,
        onNextStep = onNextStep,
        modifier = modifier
    )
}

@Composable
private fun BossContentView(
    state: BossUiState,
    onBack: () -> Unit,
    onRetryLoad: () -> Unit,
    onSubmit: (Boolean) -> Unit,
    onNext: () -> Unit,
    onRetrySave: () -> Unit,
    onNextStep: (PathStep) -> Unit,
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
                bottom = AppDimens.BottomFixed
            )
    ) {
        when (state) {
            is BossUiState.Ready -> ReadyBody(state.data, onBack, onSubmit, onNext, onRetrySave, onNextStep)
            BossUiState.Loading -> {
                BackButton(onClick = onBack)
                val loading = stringResource(R.string.lesson_loading)
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.semantics { contentDescription = loading })
                }
            }
            BossUiState.Error -> Message(R.string.lesson_error, onBack, retry = onRetryLoad)
            BossUiState.Locked -> Message(R.string.boss_locked, onBack)
            BossUiState.ComingSoon -> Message(R.string.boss_coming_soon, onBack)
        }
    }
}

// Mensaje a pantalla completa (error, bloqueado, sin contenido) con botón atrás.
@Composable
private fun Message(text: Int, onBack: () -> Unit, retry: (() -> Unit)? = null) {
    BackButton(onClick = onBack)
    Spacer(Modifier.height(AppDimens.TopLineBottom))
    Text(
        text = stringResource(text),
        style = AppTextStyles.SmallBody,
        color = Muted,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    if (retry != null) {
        Spacer(Modifier.height(AppDimens.ErrorToButton))
        PrimaryButton(text = stringResource(R.string.startup_retry), onClick = retry)
    }
}

@Composable
private fun ReadyBody(
    data: BossReady,
    onBack: () -> Unit,
    onSubmit: (Boolean) -> Unit,
    onNext: () -> Unit,
    onRetrySave: () -> Unit,
    onNextStep: (PathStep) -> Unit
) {
    val quiz = data.quiz
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onClick = onBack)
        Text(
            text = stringResource(R.string.boss_label, data.level),
            style = AppTextStyles.StepLabel,
            color = Muted
        )
        Pill(text = stringResource(R.string.lesson_xp, data.boss.xp))
    }
    Spacer(Modifier.height(AppDimens.TopLineBottom))

    BossCard(content = data.content, hpPercent = data.hpPercent, defeated = quiz.isComplete)

    Spacer(Modifier.height(AppDimens.SectionTop))
    if (quiz.isComplete) {
        VictoryCard(data = data, onRetrySave = onRetrySave)
        if (data.save is SaveState.Saved) {
            Spacer(Modifier.height(20.dp))
            if (data.nextStep != null) {
                PrimaryButton(text = stringResource(R.string.boss_next_level), onClick = { onNextStep(data.nextStep) })
            } else {
                // Último paso de la ruta: avisar que vendrán más
                MoreSoonCard()
            }
        }
    } else {
        ExerciseCard(quiz = quiz, onSubmit = onSubmit, onNext = onNext, glitchFreeRetries = false)
    }
}

// Tarjeta del jefe: emoji, nombre, presentación y barra de vida (roja, baja con cada acierto).
@Composable
private fun BossCard(content: BossContent, hpPercent: Int, defeated: Boolean) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AiVioletSoft)
            .border(AppDimens.BorderWidth, AiViolet.copy(alpha = 0.3f), shape)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (defeated) "🏆" else content.emoji, fontSize = 52.sp)
        Spacer(Modifier.height(6.dp))
        Text(text = content.name, style = AppTextStyles.ScreenTitleSmall, color = Ink, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Text(text = content.intro, style = AppTextStyles.SmallBody, color = Muted, textAlign = TextAlign.Center)
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.boss_hp), style = AppTextStyles.RowSubtitle, color = Ink)
            Text("$hpPercent %", style = AppTextStyles.RowSubtitle, color = Ink)
        }
        Spacer(Modifier.height(6.dp))
        ProgressBar(
            percent = hpPercent,
            fill = Brush.horizontalGradient(listOf(ErrorRed, AccentYellow))
        )
    }
}

// Mensaje final: el XP ganado o el estado del guardado si falló.
@Composable
private fun VictoryCard(data: BossReady, onRetrySave: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Mint)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = stringResource(R.string.boss_done_title), style = AppTextStyles.SectionTitle, color = MintInk)
        when (val save = data.save) {
            SaveState.Idle, SaveState.Saving ->
                Text(text = stringResource(R.string.lesson_saving), style = AppTextStyles.SmallBody, color = MintInk)
            is SaveState.Saved -> Text(
                text = if (save.xpAwarded > 0) stringResource(R.string.lesson_done_xp, save.xpAwarded)
                else stringResource(R.string.boss_done_review),
                style = AppTextStyles.SmallBody,
                color = MintInk
            )
            SaveState.NoConnection, SaveState.Failed -> {
                Text(
                    text = stringResource(
                        if (save == SaveState.NoConnection) R.string.lesson_save_no_connection else R.string.lesson_save_failed
                    ),
                    style = AppTextStyles.SmallBody,
                    color = FeedbackBadText
                )
                SecondaryButton(text = stringResource(R.string.startup_retry), onClick = onRetrySave)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun BossPreview() {
    val content = BossContent(
        bossId = "n01_boss",
        name = "El Bug Gigante",
        emoji = "🐛",
        intro = "Se comió media pantalla y no piensa parar. ¡Solo el código bien escrito puede detenerlo!",
        exercises = List(5) {
            MultipleChoice("Pregunta $it", listOf("a", "b", "c", "d"), 1, "bien", "mal")
        }
    )
    CodeMentorTheme {
        BossContentView(
            state = BossUiState.Ready(
                BossReady(
                    boss = Boss("n01_boss", 50),
                    content = content,
                    level = 1,
                    quiz = LessonQuiz(content.exercises).submit(true),
                    nextStep = null
                )
            ),
            onBack = {}, onRetryLoad = {}, onSubmit = {}, onNext = {}, onRetrySave = {}, onNextStep = {}
        )
    }
}
