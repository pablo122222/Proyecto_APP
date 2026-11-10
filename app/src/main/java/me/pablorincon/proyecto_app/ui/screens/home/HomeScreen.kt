package me.pablorincon.proyecto_app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.HomeSummary
import me.pablorincon.proyecto_app.domain.Lesson
import me.pablorincon.proyecto_app.domain.NextUp
import me.pablorincon.proyecto_app.domain.PathStep
import me.pablorincon.proyecto_app.domain.ReviewSuggestion
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.ui.screens.glitchcity.GlitchCityText
import me.pablorincon.proyecto_app.ui.components.AchievementUnlockedDialog
import me.pablorincon.proyecto_app.ui.components.AppCard
import me.pablorincon.proyecto_app.ui.components.Avatar
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.SecondaryButton
import me.pablorincon.proyecto_app.ui.components.SectionHeading
import me.pablorincon.proyecto_app.ui.components.StatBox
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.HeroCircle
import me.pablorincon.proyecto_app.ui.theme.HeroEnd
import me.pablorincon.proyecto_app.ui.theme.HeroEyebrow
import me.pablorincon.proyecto_app.ui.theme.HeroFill
import me.pablorincon.proyecto_app.ui.theme.HeroStart
import me.pablorincon.proyecto_app.ui.theme.HeroText
import me.pablorincon.proyecto_app.ui.theme.HeroTrack
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark
import me.pablorincon.proyecto_app.ui.theme.RowIconBackground
import me.pablorincon.proyecto_app.ui.theme.Yellow
import me.pablorincon.proyecto_app.ui.theme.YellowInk

private val HeroShape = RoundedCornerShape(18.dp)

// Pantalla "Inicio" (pestaña Inicio): saludo con racha, "Continúa donde quedaste",
// los números del día y una recomendación. Todo sale del progreso real del usuario.
@Composable
fun HomeScreen(
    onOpenStep: (PathStep) -> Unit,
    onOpenPath: () -> Unit,
    onOpenProfile: () -> Unit,
    onAskTutor: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenChallenge: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    // Al volver a Inicio (de una lección u otra pestaña) se actualizan los números
    LifecycleResumeEffect(viewModel) {
        viewModel.load(silent = true)
        onPauseOrDispose { }
    }
    HomeContent(
        state = state,
        onOpenStep = onOpenStep,
        onOpenPath = onOpenPath,
        onOpenProfile = onOpenProfile,
        onAskTutor = onAskTutor,
        onOpenChallenge = onOpenChallenge,
        onRetry = viewModel::load,
        modifier = modifier
    )
    // Logros pendientes entregados al abrir Inicio
    AchievementUnlockedDialog(
        unlocks = (state as? HomeUiState.Ready)?.unlocks.orEmpty(),
        onSeeAchievements = {
            viewModel.onUnlocksSeen()
            onOpenAchievements()
        },
        onDismiss = viewModel::onUnlocksSeen
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onOpenStep: (PathStep) -> Unit,
    onOpenPath: () -> Unit,
    onOpenProfile: () -> Unit,
    onAskTutor: () -> Unit,
    onOpenChallenge: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(
                start = AppDimens.ScreenHorizontal,
                end = AppDimens.ScreenHorizontal,
                top = AppDimens.ScreenTop,
                bottom = AppDimens.ScreenBottom // Libre para la barra de pestañas
            )
    ) {
        when (state) {
            HomeUiState.Loading -> {
                val loading = stringResource(R.string.home_loading)
                Box(Modifier.fillMaxWidth().padding(top = 96.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.semantics { contentDescription = loading })
                }
            }
            HomeUiState.Error -> {
                Text(
                    text = stringResource(R.string.home_error),
                    style = AppTextStyles.SmallBody,
                    color = Muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 96.dp)
                )
                Spacer(Modifier.height(AppDimens.ErrorToButton))
                PrimaryButton(text = stringResource(R.string.startup_retry), onClick = onRetry)
            }
            is HomeUiState.Ready -> {
                val summary = state.summary
                Greeting(summary)
                HeroCard(next = summary.next, onOpenStep = onOpenStep, onOpenPath = onOpenPath)
                SectionHeading(
                    title = stringResource(R.string.home_numbers_title),
                    linkText = stringResource(R.string.home_numbers_link),
                    onLinkClick = onOpenProfile
                )
                DayNumbers(summary)
                SectionHeading(
                    title = stringResource(R.string.home_recommended_title),
                    linkText = stringResource(R.string.home_recommended_link),
                    onLinkClick = onAskTutor
                )
                RecommendedCard(
                    recommendation = recommendationOf(summary),
                    onOpenStep = onOpenStep,
                    onAskTutor = onAskTutor,
                    onOpenChallenge = onOpenChallenge
                )
            }
        }
    }
}

// topline: avatar + "Hola, {nombre} 👋" a la izquierda y la racha (pill amarillo) a la derecha.
@Composable
private fun Greeting(summary: HomeSummary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppDimens.GreetingBottom),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(initials = summary.initials)
        Spacer(Modifier.width(AppDimens.GreetingGap))
        Column(Modifier.weight(1f)) {
            val firstName = summary.name.trim().substringBefore(' ')
            Text(
                text = if (firstName.isEmpty()) stringResource(R.string.home_greeting_no_name)
                else stringResource(R.string.home_greeting, firstName),
                style = AppTextStyles.Greeting,
                color = Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.semantics { heading() }
            )
            Spacer(Modifier.height(2.dp))
            Text(text = stringResource(R.string.home_greeting_sub), style = AppTextStyles.GreetingSub, color = Muted)
        }
        Spacer(Modifier.width(AppDimens.TopLineGap))
        val days = pluralStringResource(R.plurals.home_streak, summary.streak, summary.streak)
        val description = stringResource(R.string.home_streak_description, days)
        // Racha y, debajo y discretas, las gemas ganadas con los logros
        Column(horizontalAlignment = Alignment.End) {
            Pill(
                text = "🔥 $days",
                containerColor = Yellow,
                contentColor = YellowInk,
                modifier = Modifier.semantics { contentDescription = description }
            )
            Spacer(Modifier.height(5.dp))
            val gemsDescription = stringResource(R.string.gems_description, summary.gems)
            Pill(
                text = stringResource(R.string.gems_pill, summary.gems),
                modifier = Modifier.semantics { contentDescription = gemsDescription }
            )
        }
    }
}

// Tarjeta hero: el siguiente paso de la ruta con su avance, o el estado de la ruta.
@Composable
private fun HeroCard(next: NextUp, onOpenStep: (PathStep) -> Unit, onOpenPath: () -> Unit) {
    val levelTitles = stringArrayResource(R.array.level_titles)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(HeroShape)
            .background(Brush.linearGradient(listOf(HeroStart, HeroEnd)))
    ) {
        // Círculo decorativo arriba a la derecha
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 30.dp, y = (-40).dp)
                .size(AppDimens.HeroCircle)
                .clip(CircleShape)
                .background(HeroCircle)
        )
        Column(Modifier.padding(AppDimens.HeroPadding)) {
            when (next) {
                is NextUp.Step -> {
                    val levelTitle = levelTitles.getOrElse(next.level - 1) { "" }
                    HeroEyebrowText(
                        stringResource(if (next.firstTime) R.string.home_hero_eyebrow_first else R.string.home_hero_eyebrow)
                    )
                    HeroTitleText(next.title)
                    HeroBodyText(
                        when {
                            next.isBoss -> stringResource(R.string.home_hero_boss_text, next.level)
                            next.firstTime -> stringResource(R.string.home_hero_first_text, next.level, levelTitle)
                            else -> stringResource(R.string.home_hero_level, next.level, levelTitle)
                        }
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = stringResource(R.string.home_hero_progress_label), style = AppTextStyles.HeroText, color = HeroText)
                        Text(
                            text = stringResource(R.string.home_hero_progress, next.doneInLevel, next.stepsInLevel),
                            style = AppTextStyles.HeroText,
                            color = Paper
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    HeroBar(fraction = next.doneInLevel.toFloat() / next.stepsInLevel)
                    Spacer(Modifier.height(14.dp))
                    HeroButton(
                        text = stringResource(
                            when {
                                next.isBoss -> R.string.home_hero_boss
                                next.firstTime -> R.string.home_hero_start
                                else -> R.string.home_hero_continue
                            }
                        ),
                        onClick = { onOpenStep(next.step) }
                    )
                }
                is NextUp.ComingSoon -> {
                    HeroEyebrowText(stringResource(R.string.home_hero_eyebrow))
                    HeroTitleText(stringResource(R.string.home_soon_title))
                    HeroBodyText(stringResource(R.string.home_soon_text, next.level))
                    Spacer(Modifier.height(14.dp))
                    HeroButton(text = stringResource(R.string.home_see_path), onClick = onOpenPath)
                }
                NextUp.Finished -> {
                    HeroEyebrowText(stringResource(R.string.home_hero_eyebrow))
                    HeroTitleText(stringResource(R.string.home_finished_title))
                    HeroBodyText(stringResource(R.string.home_finished_text, Curriculum.LEVEL_COUNT))
                    Spacer(Modifier.height(14.dp))
                    HeroButton(text = stringResource(R.string.home_see_path), onClick = onOpenPath)
                }
            }
        }
    }
}

@Composable
private fun HeroEyebrowText(text: String) {
    Text(text = text.uppercase(), style = AppTextStyles.Eyebrow, color = HeroEyebrow)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun HeroTitleText(text: String) {
    Text(
        text = text,
        style = AppTextStyles.HeroTitle,
        color = Paper,
        modifier = Modifier.widthIn(max = AppDimens.HeroTitleMaxWidth)
    )
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun HeroBodyText(text: String) {
    Text(
        text = text,
        style = AppTextStyles.HeroText,
        color = HeroText,
        modifier = Modifier.widthIn(max = AppDimens.HeroTextMaxWidth)
    )
}

// Barra del hero: pista blanca al 19 % y relleno verde.
@Composable
private fun HeroBar(fraction: Float) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(AppDimens.HeroBarHeight)
            .clip(CircleShape)
            .background(HeroTrack)
    ) {
        Box(
            Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .height(AppDimens.HeroBarHeight)
                .clip(CircleShape)
                .background(HeroFill)
        )
    }
}

// Botón blanco con texto azul (alto 40).
@Composable
private fun HeroButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .widthIn(max = AppDimens.HeroButtonMaxWidth)
            .heightIn(min = AppDimens.HeroButtonHeight)
            .clip(RoundedCornerShape(12.dp))
            .background(Paper)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = AppTextStyles.HeroButton, color = PrimaryDark, maxLines = 1)
    }
}

// "Tu día en números": XP y actividades de hoy frente a la meta diaria.
@Composable
private fun DayNumbers(summary: HomeSummary) {
    Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.StatGap)) {
        StatBox(
            emoji = "⚡",
            value = summary.xp.toString(),
            caption = stringResource(R.string.home_xp_caption),
            modifier = Modifier.weight(1f)
        )
        StatBox(
            emoji = "🎯",
            value = stringResource(R.string.home_today_value, summary.todayDone, summary.dailyGoal),
            caption = stringResource(if (summary.goalMet) R.string.home_today_done else R.string.home_today_caption),
            captionColor = if (summary.goalMet) MintInk else Muted,
            modifier = Modifier.weight(1f)
        )
    }
}

// Qué recomienda la tarjeta, en orden: un reto de Glitch City, repasar la última lección
// completada o, si no hay nada de eso, preguntar al tutor.
private sealed interface Recommendation {
    data class Challenge(val activityId: String) : Recommendation
    data class Review(val review: ReviewSuggestion) : Recommendation
    data object Tutor : Recommendation
}

private fun recommendationOf(summary: HomeSummary): Recommendation = when {
    summary.challengeId != null -> Recommendation.Challenge(summary.challengeId)
    summary.review != null -> Recommendation.Review(summary.review)
    else -> Recommendation.Tutor
}

@Composable
private fun RecommendedCard(
    recommendation: Recommendation,
    onOpenStep: (PathStep) -> Unit,
    onAskTutor: () -> Unit,
    onOpenChallenge: (String) -> Unit
) {
    when (recommendation) {
        is Recommendation.Challenge -> ChallengeCard(recommendation.activityId) { onOpenChallenge(recommendation.activityId) }
        is Recommendation.Review -> ReviewOrTutorCard(recommendation.review, onOpenStep, onAskTutor)
        Recommendation.Tutor -> ReviewOrTutorCard(null, onOpenStep, onAskTutor)
    }
}

// Reto de Glitch City: nivel del dron, laboratorio con descubrimientos pendientes o creación sin publicar.
@Composable
private fun ChallengeCard(activityId: String, onOpen: () -> Unit) {
    val lab = GlitchCityCatalog.lab(activityId)
    val creation = GlitchCityCatalog.creation(activityId)
    val (emoji, nameRes, textRes) = when {
        lab != null -> Triple(lab.emoji, GlitchCityText.activityNames.getValue(activityId).first, R.string.home_challenge_lab_text)
        creation != null -> Triple(creation.emoji, GlitchCityText.activityNames.getValue(activityId).first, R.string.home_challenge_creation_text)
        else -> Triple("🚁", GlitchCityText.droneLevelNames.getValue(activityId), R.string.home_challenge_drone_text)
    }
    RecommendationLayout(
        emoji = emoji,
        title = stringResource(R.string.home_challenge_title, stringResource(nameRes)),
        text = stringResource(textRes),
        button = stringResource(R.string.home_challenge_button),
        onClick = onOpen
    )
}

// Repasar la última lección completada; sin ninguna, preguntar al tutor.
@Composable
private fun ReviewOrTutorCard(review: ReviewSuggestion?, onOpenStep: (PathStep) -> Unit, onAskTutor: () -> Unit) {
    if (review != null) {
        RecommendationLayout(
            emoji = "✨",
            title = stringResource(R.string.home_review_title, review.title),
            text = stringResource(R.string.home_review_text),
            button = stringResource(R.string.home_review_button),
            onClick = { onOpenStep(review.lesson) }
        )
    } else {
        RecommendationLayout(
            emoji = "✨",
            title = stringResource(R.string.home_tutor_title),
            text = stringResource(R.string.home_tutor_text),
            button = stringResource(R.string.home_tutor_button),
            onClick = onAskTutor
        )
    }
}

@Composable
private fun RecommendationLayout(emoji: String, title: String, text: String, button: String, onClick: () -> Unit) {
    AppCard {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(AppDimens.RecommendIcon)
                    .clip(RoundedCornerShape(14.dp))
                    .background(RowIconBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, style = AppTextStyles.RowIcon)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(text = title, style = AppTextStyles.RecommendTitle, color = Ink)
                Spacer(Modifier.height(4.dp))
                Text(text = text, style = AppTextStyles.RowSubtitle, color = Muted)
                Spacer(Modifier.height(12.dp))
                SecondaryButton(text = button, onClick = onClick)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    val lesson = Lesson("n01_l03", 20)
    CodeMentorTheme {
        HomeContent(
            state = HomeUiState.Ready(
                HomeSummary(
                    name = "Juan David",
                    initials = "JD",
                    streak = 3,
                    xp = 120,
                    gems = 25,
                    todayDone = 1,
                    dailyGoal = 2,
                    next = NextUp.Step(lesson, level = 1, title = "Glitch no lee mentes 🤖", doneInLevel = 2, stepsInLevel = 12, firstTime = false),
                    review = ReviewSuggestion(Lesson("n01_l02", 20), "Comillas mágicas"),
                    challengeId = "d2"
                )
            ),
            onOpenChallenge = {},
            onOpenStep = {},
            onOpenPath = {},
            onOpenProfile = {},
            onAskTutor = {},
            onRetry = {}
        )
    }
}
