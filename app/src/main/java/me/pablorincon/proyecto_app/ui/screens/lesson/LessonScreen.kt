package me.pablorincon.proyecto_app.ui.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.Lesson
import me.pablorincon.proyecto_app.domain.LessonQuiz
import me.pablorincon.proyecto_app.domain.PathStep

import me.pablorincon.proyecto_app.domain.content.LessonCatalog
import me.pablorincon.proyecto_app.ui.components.BackButton
import me.pablorincon.proyecto_app.ui.components.MoreSoonCard
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ProgressBar
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.components.SectionHeading
import me.pablorincon.proyecto_app.ui.theme.AnswerCorrectBg
import me.pablorincon.proyecto_app.ui.theme.AnswerCorrectBorder
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBg
import me.pablorincon.proyecto_app.ui.theme.AnswerWrongBorder
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeBg
import me.pablorincon.proyecto_app.ui.theme.CodeLineNumber
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.CodeText
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackBadText
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodBg
import me.pablorincon.proyecto_app.ui.theme.FeedbackGoodText
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.OutputBg
import me.pablorincon.proyecto_app.ui.theme.OutputBorder
import me.pablorincon.proyecto_app.ui.theme.OutputText
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark
import me.pablorincon.proyecto_app.ui.theme.SoftBlueCardBg
import me.pablorincon.proyecto_app.ui.theme.SoftBlueCardBorder
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk

// Pantalla genérica de lección (guía §4): se alimenta del currículo y del catálogo de contenido.
@Composable
fun LessonScreen(
    lessonId: String,
    onBack: () -> Unit,
    onNextLesson: (PathStep) -> Unit,
    onPractice: () -> Unit,
    onAskTutor: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: LessonViewModel = viewModel(
        key = lessonId,
        factory = viewModelFactory { initializer { LessonViewModel(lessonId) } }
    )
    val state by viewModel.uiState.collectAsState()
    LessonContentView(
        state = state,
        onBack = onBack,
        onRetryLoad = viewModel::load,
        onSubmit = viewModel::onSubmit,
        onNext = viewModel::onNext,
        onRetrySave = viewModel::onRetrySave,
        onNextLesson = onNextLesson,
        onPractice = onPractice,
        onAskTutor = onAskTutor,
        modifier = modifier
    )
}

@Composable
private fun LessonContentView(
    state: LessonUiState,
    onBack: () -> Unit,
    onRetryLoad: () -> Unit,
    onSubmit: (Boolean) -> Unit,
    onNext: () -> Unit,
    onRetrySave: () -> Unit,
    onNextLesson: (PathStep) -> Unit,
    onPractice: () -> Unit,
    onAskTutor: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            // La lección no lleva barra de pestañas: solo un margen final normal
            .padding(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = AppDimens.BottomFixed
            )
    ) {
        when (state) {
            is LessonUiState.Ready -> ReadyBody(
                data = state.data,
                onBack = onBack,
                onSubmit = onSubmit,
                onNext = onNext,
                onRetrySave = onRetrySave,
                onNextLesson = onNextLesson,
                onPractice = onPractice,
                onAskTutor = onAskTutor
            )
            LessonUiState.Loading -> {
                BackButton(onClick = onBack)
                val loading = stringResource(R.string.lesson_loading)
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.semantics { contentDescription = loading })
                }
            }
            LessonUiState.Error -> Message(R.string.lesson_error, onBack, retry = onRetryLoad)
            LessonUiState.Locked -> Message(R.string.lesson_locked, onBack)
            LessonUiState.ComingSoon -> Message(R.string.lesson_coming_soon, onBack)
        }
    }
}

// Mensaje a pantalla completa (error, bloqueada, sin contenido) con botón atrás.
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
    data: LessonReady,
    onBack: () -> Unit,
    onSubmit: (Boolean) -> Unit,
    onNext: () -> Unit,
    onRetrySave: () -> Unit,
    onNextLesson: (PathStep) -> Unit,
    onPractice: () -> Unit,
    onAskTutor: () -> Unit
) {
    val quiz = data.quiz
    val content = data.content

    // topline: atrás · "Lección X de N" · "+20 XP"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onClick = onBack)
        Text(
            text = stringResource(R.string.lesson_x_of_n, data.numberInModule, data.moduleLessonCount),
            style = AppTextStyles.StepLabel,
            color = Muted
        )
        Pill(text = stringResource(R.string.lesson_xp, data.lesson.xp))
    }
    Spacer(Modifier.height(AppDimens.TopLineBottom))

    // Progreso de la lección = preguntas acertadas
    ProgressBar(percent = quiz.correctCount * 100 / quiz.total)
    Spacer(Modifier.height(21.dp))

    Text(text = stringResource(R.string.lesson_eyebrow).uppercase(), style = AppTextStyles.Eyebrow, color = Primary)
    Spacer(Modifier.height(AppDimens.EyebrowBottom))
    Text(text = content.title, style = AppTextStyles.ScreenTitle, color = Ink)
    Spacer(Modifier.height(AppDimens.TitleBottom))
    Text(text = content.hook, style = AppTextStyles.Subtitle, color = Muted)
    Spacer(Modifier.height(8.dp))
    Text(text = content.explanation, style = AppTextStyles.Subtitle, color = Ink)

    Spacer(Modifier.height(20.dp))
    CodeBlock(code = content.code)
    Spacer(Modifier.height(12.dp))
    ExpectedOutput(output = content.expectedOutput)

    SectionHeading(title = stringResource(R.string.lesson_check_title))
    if (quiz.isComplete) {
        CompletedCard(data = data, onRetrySave = onRetrySave)
    } else {
        ExerciseCard(quiz = quiz, onSubmit = onSubmit, onNext = onNext)
    }

    Spacer(Modifier.height(AppDimens.SectionTop))
    TutorCard(onAskTutor = onAskTutor)

    // Al terminar: seguir con la siguiente lección o practicar
    if (quiz.isComplete && data.save is SaveState.Saved) {
        Spacer(Modifier.height(20.dp))
        if (data.nextStep != null) {
            PrimaryButton(
                text = stringResource(R.string.lesson_next),
                onClick = { onNextLesson(data.nextStep) }
            )
            Spacer(Modifier.height(10.dp))
            SecondaryButton(text = stringResource(R.string.lesson_practice), onClick = onPractice)
        } else {
            PrimaryButton(text = stringResource(R.string.lesson_practice), onClick = onPractice)
            // Última lección de la ruta: avisar que vendrán más
            Spacer(Modifier.height(AppDimens.ModuleGap))
            MoreSoonCard()
        }
    }
}

@Composable
private fun ExpectedOutput(output: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(OutputBg)
            .border(AppDimens.BorderWidth, OutputBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.lesson_expected_output),
            style = AppTextStyles.RowTitle,
            color = OutputText
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = output,
            style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp, lineHeight = 17.sp),
            color = OutputText
        )
    }
}

// Mensaje final: XP ganado, o el estado del guardado si falló.
@Composable
internal fun CompletedCard(data: LessonReady, onRetrySave: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Mint)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = stringResource(R.string.lesson_done_title), style = AppTextStyles.SectionTitle, color = MintInk)
        when (val save = data.save) {
            SaveState.Idle, SaveState.Saving ->
                Text(text = stringResource(R.string.lesson_saving), style = AppTextStyles.SmallBody, color = MintInk)
            is SaveState.Saved -> Text(
                text = if (save.xpAwarded > 0) stringResource(R.string.lesson_done_xp, save.xpAwarded)
                else stringResource(R.string.lesson_done_review),
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

// Tarjeta azul suave: "¿Necesitas una explicación diferente?"
@Composable
private fun TutorCard(onAskTutor: () -> Unit) {
    val shape = RoundedCornerShape(15.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SoftBlueCardBg)
            .border(AppDimens.BorderWidth, SoftBlueCardBorder, shape)
            .padding(14.dp)
    ) {
        Text(text = stringResource(R.string.lesson_tutor_title), style = AppTextStyles.CardTitleSmall, color = PrimaryDark)
        Spacer(Modifier.height(4.dp))
        Text(text = stringResource(R.string.lesson_tutor_text), style = AppTextStyles.RowSubtitle, color = Muted)
        Spacer(Modifier.height(10.dp))
        SecondaryButton(text = stringResource(R.string.lesson_tutor_button), onClick = onAskTutor)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LessonPreview() {
    val content = LessonCatalog.contentFor("n01_l01")!!
    CodeMentorTheme {
        LessonContentView(
            state = LessonUiState.Ready(
                LessonReady(
                    lesson = Lesson("n01_l01", 20),
                    content = content,
                    numberInModule = 1,
                    moduleLessonCount = 4,
                    quiz = LessonQuiz(content.exercises).submit(true),
                    nextStep = Lesson("n01_l02", 20)
                )
            ),
            onBack = {}, onRetryLoad = {}, onSubmit = {}, onNext = {}, onRetrySave = {},
            onNextLesson = {}, onPractice = {}, onAskTutor = {}
        )
    }
}
