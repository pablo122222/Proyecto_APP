package me.pablorincon.proyecto_app.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.BuildConfig
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.DailyGoal
import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel
import me.pablorincon.proyecto_app.ui.components.AppTextField
import me.pablorincon.proyecto_app.ui.components.ChoiceOption
import me.pablorincon.proyecto_app.ui.components.FormError
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.SettingsBandDivider
import me.pablorincon.proyecto_app.ui.components.SettingsHeader
import me.pablorincon.proyecto_app.ui.components.SettingsNote
import me.pablorincon.proyecto_app.ui.components.SettingsRow
import me.pablorincon.proyecto_app.ui.components.SettingsScaffold
import me.pablorincon.proyecto_app.ui.components.StatBox
import me.pablorincon.proyecto_app.ui.screens.personalization.iconRes
import me.pablorincon.proyecto_app.ui.screens.personalization.subtitleRes
import me.pablorincon.proyecto_app.ui.screens.personalization.titleRes
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Muted

// Pantallas internas de Perfil. Todas usan el ViewModel de Perfil (mismo grafo) y el estilo
// de lista de ajustes. Muestran un aviso corto (toast) al guardar o si algo falla.

// Avisos de las acciones de Perfil.
@Composable
private fun ProfileMessages(viewModel: ProfileViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val text = when (state.message) {
        ProfileMessage.Saved -> stringResource(R.string.profile_saved)
        ProfileMessage.ResetEmailSent -> stringResource(R.string.profile_reset_email_sent)
        ProfileMessage.NoConnection -> stringResource(R.string.settings_no_connection)
        ProfileMessage.Failed -> stringResource(R.string.settings_failed)
        ProfileMessage.InvalidName, null -> null // El nombre inválido se muestra bajo el campo
    }
    LaunchedEffect(text) {
        if (text != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }
}

@Composable
private fun Body(content: @Composable ColumnScope.() -> Unit) =
    Column(Modifier.padding(horizontal = AppDimens.ScreenHorizontal, vertical = 8.dp), content = content)

// ---------- Editar perfil: nombre visible y correo ----------
@Composable
fun EditProfileScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val summary = state.summary
    var name by rememberSaveable(summary?.profile?.name) { mutableStateOf(summary?.profile?.name.orEmpty()) }
    ProfileMessages(viewModel)
    SettingsScaffold(title = stringResource(R.string.profile_edit_title), onBack = onBack) {
        Body {
            AppTextField(
                label = stringResource(R.string.profile_edit_name_label),
                value = name,
                onValueChange = { name = it.take(40) },
                placeholder = stringResource(R.string.profile_edit_name_placeholder),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )
            FormError(message = if (state.message == ProfileMessage.InvalidName) stringResource(R.string.profile_edit_name_invalid) else null)
            Spacer(Modifier.height(8.dp))
            Text(text = stringResource(R.string.profile_edit_email_label), style = AppTextStyles.FieldLabel, color = Ink)
            Spacer(Modifier.height(6.dp))
            Text(text = summary?.email.orEmpty(), style = AppTextStyles.SettingsTitle, color = Muted)
            Spacer(Modifier.height(4.dp))
            Text(text = stringResource(R.string.profile_edit_email_note), style = AppTextStyles.SettingsNote, color = Muted)
            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = stringResource(if (state.saving) R.string.settings_working else R.string.settings_save),
                enabled = !state.saving && name.isNotBlank() && name.trim() != summary?.profile?.name,
                onClick = { viewModel.saveName(name, onDone = onBack) }
            )
        }
    }
}

// ---------- Contraseña y seguridad ----------
@Composable
fun SecurityScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    ProfileMessages(viewModel)
    SettingsScaffold(title = stringResource(R.string.profile_security_title), onBack = onBack) {
        SettingsHeader(stringResource(R.string.profile_security_signin))
        SettingsRow(
            icon = R.drawable.ic_row_mail,
            title = stringResource(R.string.profile_security_email),
            subtitle = state.summary?.email.orEmpty(),
            showChevron = false,
            onClick = {}
        )
        SettingsBandDivider()
        SettingsHeader(stringResource(R.string.profile_security_password))
        SettingsRow(
            icon = R.drawable.ic_row_lock,
            title = stringResource(R.string.profile_security_change_password),
            subtitle = stringResource(
                if (state.saving) R.string.settings_working else R.string.profile_security_change_password_subtitle
            ),
            enabled = !state.saving,
            onClick = viewModel::sendPasswordReset
        )
        SettingsNote(stringResource(R.string.profile_security_note))
    }
}

// ---------- Meta diaria ----------
@Composable
fun DailyGoalScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val current = state.summary?.profile?.dailyGoal
    var selected by rememberSaveable(current) { mutableIntStateOf(current ?: 2) }
    ProfileMessages(viewModel)
    SettingsScaffold(title = stringResource(R.string.profile_daily_goal_title), onBack = onBack) {
        Body {
            Text(text = stringResource(R.string.profile_daily_goal_text), style = AppTextStyles.Subtitle, color = Muted)
            Spacer(Modifier.height(16.dp))
            Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(AppDimens.ChoiceGap)) {
                DailyGoal.options.forEach { option ->
                    ChoiceOption(
                        icon = dailyGoalIcon(option),
                        title = stringResource(R.string.profile_daily_goal_value, option),
                        subtitle = stringResource(dailyGoalPace(option)),
                        selected = selected == option,
                        onClick = { selected = option }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(text = stringResource(R.string.profile_daily_goal_note), style = AppTextStyles.SettingsNote, color = Muted)
            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = stringResource(if (state.saving) R.string.settings_working else R.string.settings_save),
                enabled = !state.saving && selected != current,
                onClick = { viewModel.saveDailyGoal(selected, onDone = onBack) }
            )
        }
    }
}

private fun dailyGoalIcon(goal: Int) = when (goal) {
    1 -> "🌿"
    2 -> "🎯"
    3 -> "⚡"
    else -> "🚀"
}

// Ritmos de la guía §7 ("Ritmo tranquilo", "Ritmo recomendado"...).
private fun dailyGoalPace(goal: Int) = when (goal) {
    1 -> R.string.daily_goal_pace_1
    2 -> R.string.daily_goal_pace_2
    3 -> R.string.daily_goal_pace_3
    else -> R.string.daily_goal_pace_5
}

// ---------- Nivel y objetivo (los de Personalización) ----------
@Composable
fun LevelGoalScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val profile = state.summary?.profile
    var level by rememberSaveable(profile?.level) { mutableStateOf(profile?.level ?: LearningLevel.Beginner) }
    var goal by rememberSaveable(profile?.goal) { mutableStateOf(profile?.goal ?: LearningGoal.UnderstandProgramming) }
    ProfileMessages(viewModel)
    SettingsScaffold(title = stringResource(R.string.profile_level_goal_title), onBack = onBack) {
        Body {
            Text(text = stringResource(R.string.profile_level_goal_text), style = AppTextStyles.Subtitle, color = Muted)
            Spacer(Modifier.height(12.dp))
            Text(text = stringResource(R.string.profile_level_label), style = AppTextStyles.SectionTitle, color = Ink)
            Spacer(Modifier.height(10.dp))
            Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(AppDimens.ChoiceGap)) {
                LearningLevel.entries.forEach { option ->
                    ChoiceOption(
                        icon = stringResource(option.iconRes),
                        title = stringResource(option.titleRes),
                        subtitle = stringResource(option.subtitleRes),
                        selected = level == option,
                        onClick = { level = option }
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Text(text = stringResource(R.string.profile_goal_label), style = AppTextStyles.SectionTitle, color = Ink)
            Spacer(Modifier.height(10.dp))
            Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(AppDimens.ChoiceGap)) {
                LearningGoal.entries.forEach { option ->
                    ChoiceOption(
                        icon = stringResource(option.iconRes),
                        title = stringResource(option.titleRes),
                        subtitle = stringResource(option.subtitleRes),
                        selected = goal == option,
                        onClick = { goal = option }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(text = stringResource(R.string.profile_level_goal_note), style = AppTextStyles.SettingsNote, color = Muted)
            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = stringResource(if (state.saving) R.string.settings_working else R.string.settings_save),
                enabled = !state.saving && (level != profile?.level || goal != profile.goal),
                onClick = { viewModel.saveLevelAndGoal(level, goal, onDone = onBack) }
            )
        }
    }
}

// ---------- Mis estadísticas ----------
@Composable
fun StatsScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val s = state.summary
    SettingsScaffold(title = stringResource(R.string.profile_stats_title), onBack = onBack) {
        if (s == null) return@SettingsScaffold
        Body {
            Text(text = stringResource(R.string.profile_stats_text), style = AppTextStyles.Subtitle, color = Muted)
            Spacer(Modifier.height(16.dp))
            StatPair(
                "📘", stringResource(R.string.stats_value_of, s.lessonsCompleted, s.totalLessons), stringResource(R.string.stats_lessons),
                "👾", stringResource(R.string.stats_value_of, s.bossesDefeated, s.totalLevels), stringResource(R.string.stats_bosses)
            )
            StatPair(
                "⚡", s.profile.xp.toString(), stringResource(R.string.stats_xp),
                "🏅", stringResource(R.string.profile_level_short, s.userLevel.level), stringResource(rankRes(s.userLevel.rank))
            )
            StatPair(
                "🔥", s.streak.toString(), stringResource(R.string.stats_streak),
                "🏆", s.profile.bestStreak.coerceAtLeast(s.streak).toString(), stringResource(R.string.stats_best_streak)
            )
            StatPair(
                "🐛", s.profile.bugsFound.toString(), stringResource(R.string.stats_bugs),
                "🔁", s.profile.recoveredExercises.toString(), stringResource(R.string.stats_recovered)
            )
            StatPair(
                "◆", s.profile.gems.toString(), stringResource(R.string.stats_gems),
                "🎖️", stringResource(R.string.stats_value_of, s.achievementsUnlocked, s.achievementsAvailable), stringResource(R.string.stats_achievements)
            )
        }
    }
}

@Composable
private fun StatPair(e1: String, v1: String, c1: String, e2: String, v2: String, c2: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.StatGap)) {
        StatBox(emoji = e1, value = v1, caption = c1, modifier = Modifier.weight(1f))
        StatBox(emoji = e2, value = v2, caption = c2, modifier = Modifier.weight(1f))
    }
    Spacer(Modifier.height(AppDimens.StatGap))
}

// ---------- Acerca de CodeMentor AI ----------
@Composable
fun AboutScreen(onBack: () -> Unit) {
    SettingsScaffold(title = stringResource(R.string.profile_about_title), onBack = onBack) {
        Body {
            Text(text = stringResource(R.string.app_name), style = AppTextStyles.ScreenTitleSmall, color = Ink)
            Spacer(Modifier.height(4.dp))
            Text(text = stringResource(R.string.about_version, BuildConfig.VERSION_NAME), style = AppTextStyles.RowSubtitle, color = Muted)
            Spacer(Modifier.height(14.dp))
            Text(text = stringResource(R.string.about_text), style = AppTextStyles.Subtitle, color = Ink)
        }
        SettingsBandDivider()
        SettingsHeader(stringResource(R.string.about_how_title))
        SettingsNote(stringResource(R.string.about_how_text))
        SettingsBandDivider()
        SettingsHeader(stringResource(R.string.about_credits_title))
        SettingsNote(stringResource(R.string.about_credits_text))
    }
}
