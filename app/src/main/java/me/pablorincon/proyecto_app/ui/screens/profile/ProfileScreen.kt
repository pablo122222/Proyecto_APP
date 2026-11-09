package me.pablorincon.proyecto_app.ui.screens.profile

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.ProfileSummary
import me.pablorincon.proyecto_app.domain.UserRank
import me.pablorincon.proyecto_app.ui.components.Avatar
import me.pablorincon.proyecto_app.ui.components.ConfirmSheet
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ProgressBar
import me.pablorincon.proyecto_app.ui.components.SettingsBandDivider
import me.pablorincon.proyecto_app.ui.components.SettingsHeader
import me.pablorincon.proyecto_app.ui.components.SettingsRow
import me.pablorincon.proyecto_app.ui.screens.personalization.valueRes
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary

// Destinos internos de Perfil.
data class ProfileActions(
    val onEditProfile: () -> Unit,
    val onSecurity: () -> Unit,
    val onDailyGoal: () -> Unit,
    val onLevelGoal: () -> Unit,
    val onStats: () -> Unit,
    val onAchievements: () -> Unit,
    val onSettings: () -> Unit,
    val onAbout: () -> Unit,
    val onSignedOut: () -> Unit
)

// Pantalla Perfil (pestaña Perfil): cabecera con avatar, nivel y XP, y una lista de opciones
// a ancho completo (cuenta, aprendizaje y app), cada una con su pantalla.
@Composable
fun ProfileScreen(actions: ProfileActions, viewModel: ProfileViewModel) {
    val state by viewModel.uiState.collectAsState()
    var confirmingSignOut by rememberSaveable { mutableStateOf(false) }

    LifecycleResumeEffect(viewModel) {
        viewModel.load(silent = true)
        onPauseOrDispose { }
    }
    LaunchedEffect(state.signedOut) {
        if (state.signedOut) {
            viewModel.onSignedOutHandled()
            actions.onSignedOut()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(bottom = AppDimens.ScreenBottom) // Libre para la barra de pestañas
    ) {
        // topline: "Mi perfil" + botón ⚙ (abre Configuración)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = AppDimens.ScreenHorizontal, end = AppDimens.ScreenHorizontal, top = AppDimens.ScreenTop),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profile_title),
                style = AppTextStyles.ScreenTitleSmall,
                color = Ink,
                modifier = Modifier.weight(1f)
            )
            val settings = stringResource(R.string.title_settings)
            Box(
                modifier = Modifier
                    .size(AppDimens.BackButtonSize)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Paper)
                    .border(AppDimens.BorderWidth, Line, RoundedCornerShape(12.dp))
                    .clickable(role = Role.Button, onClick = actions.onSettings)
                    .semantics { contentDescription = settings },
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(R.drawable.ic_row_settings), contentDescription = null, tint = Ink, modifier = Modifier.size(20.dp))
            }
        }

        val summary = state.summary
        when {
            summary != null -> {
                ProfileHeader(summary)
                ProfileOptions(summary, actions, onSignOut = { confirmingSignOut = true })
            }
            state.loadFailed -> Column(Modifier.padding(AppDimens.ScreenHorizontal)) {
                Text(
                    text = stringResource(R.string.home_error),
                    style = AppTextStyles.SmallBody,
                    color = Muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp)
                )
                Spacer(Modifier.height(AppDimens.ErrorToButton))
                PrimaryButton(text = stringResource(R.string.startup_retry), onClick = { viewModel.load() })
            }
            else -> Box(Modifier.fillMaxWidth().padding(top = 96.dp), Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        }
    }

    if (confirmingSignOut) {
        ConfirmSheet(
            title = stringResource(R.string.logout_confirm_title),
            message = stringResource(R.string.logout_confirm_message),
            confirmLabel = stringResource(R.string.logout_confirm_yes),
            cancelLabel = stringResource(R.string.logout_confirm_cancel),
            onConfirm = {
                confirmingSignOut = false
                viewModel.signOut()
            },
            onDismiss = { confirmingSignOut = false }
        )
    }
}

// Héroe: avatar, nombre, nivel y rango, barra de XP y tres números rápidos.
@Composable
private fun ProfileHeader(summary: ProfileSummary) {
    val level = summary.userLevel
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = AppDimens.ScreenHorizontal, end = AppDimens.ScreenHorizontal, top = 14.dp, bottom = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(initials = summary.initials, size = 76.dp, corner = 25.dp, fontSize = 30.sp)
        Spacer(Modifier.height(10.dp))
        Text(text = summary.profile.name, style = AppTextStyles.Greeting.copy(fontSize = AppTextStyles.Greeting.fontSize * 0.9f), color = Ink)
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(R.string.profile_level_rank, level.level, stringResource(rankRes(level.rank))),
            style = AppTextStyles.GreetingSub,
            color = Muted
        )
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.profile_xp_to_next, level.xpToNext), style = AppTextStyles.RowSubtitle, color = Muted)
            Text(text = stringResource(R.string.profile_xp_progress, level.xpInLevel, level.xpForNext), style = AppTextStyles.RowSubtitle, color = Ink)
        }
        Spacer(Modifier.height(6.dp))
        ProgressBar(percent = level.percent)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            QuickStat(value = stringResource(R.string.profile_stat_xp, summary.profile.xp), label = stringResource(R.string.profile_stat_xp_label))
            QuickStat(
                value = "🔥 ${summary.streak}",
                label = pluralStringResource(R.plurals.profile_stat_streak_label, summary.streak)
            )
            QuickStat(value = "📘 ${summary.lessonsCompleted}", label = stringResource(R.string.profile_stat_lessons_label))
            QuickStat(value = stringResource(R.string.gems_pill, summary.profile.gems), label = stringResource(R.string.profile_stat_gems_label))
        }
    }
}

@Composable
private fun QuickStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(text = value, style = AppTextStyles.RecommendTitle.copy(fontSize = AppTextStyles.SettingsTitle.fontSize), color = Ink)
        Spacer(Modifier.height(2.dp))
        Text(text = label, style = AppTextStyles.StatCaption, color = Muted)
    }
}

internal fun rankRes(rank: UserRank): Int = when (rank) {
    UserRank.Apprentice -> R.string.rank_apprentice
    UserRank.Explorer -> R.string.rank_explorer
    UserRank.Builder -> R.string.rank_builder
    UserRank.Expert -> R.string.rank_expert
}

// Lista de opciones por secciones (estilo lista a ancho completo).
@Composable
private fun ProfileOptions(summary: ProfileSummary, actions: ProfileActions, onSignOut: () -> Unit) {
    val profile = summary.profile
    SettingsBandDivider()
    SettingsHeader(stringResource(R.string.profile_section_account))
    SettingsRow(
        icon = R.drawable.ic_row_user,
        title = stringResource(R.string.profile_edit_title),
        subtitle = stringResource(R.string.profile_edit_subtitle),
        onClick = actions.onEditProfile
    )
    SettingsRow(
        icon = R.drawable.ic_row_shield,
        title = stringResource(R.string.profile_security_title),
        subtitle = stringResource(R.string.profile_security_subtitle),
        onClick = actions.onSecurity
    )

    SettingsBandDivider()
    SettingsHeader(stringResource(R.string.profile_section_learning))
    SettingsRow(
        icon = R.drawable.ic_row_target,
        title = stringResource(R.string.profile_daily_goal_title),
        value = stringResource(R.string.profile_daily_goal_value, profile.dailyGoal),
        onClick = actions.onDailyGoal
    )
    SettingsRow(
        icon = R.drawable.ic_row_compass,
        title = stringResource(R.string.profile_level_goal_title),
        value = profile.level?.let { stringResource(it.valueRes) },
        onClick = actions.onLevelGoal
    )
    SettingsRow(
        icon = R.drawable.ic_row_chart,
        title = stringResource(R.string.profile_stats_title),
        subtitle = stringResource(R.string.profile_stats_subtitle),
        onClick = actions.onStats
    )
    SettingsRow(
        icon = R.drawable.ic_row_trophy,
        title = stringResource(R.string.profile_achievements_title),
        value = stringResource(R.string.profile_achievements_value, summary.achievementsUnlocked, summary.achievementsAvailable),
        onClick = actions.onAchievements
    )

    SettingsBandDivider()
    SettingsHeader(stringResource(R.string.profile_section_app))
    SettingsRow(
        icon = R.drawable.ic_row_settings,
        title = stringResource(R.string.profile_settings_title),
        subtitle = stringResource(R.string.profile_settings_subtitle),
        onClick = actions.onSettings
    )
    SettingsRow(
        icon = R.drawable.ic_row_info,
        title = stringResource(R.string.profile_about_title),
        onClick = actions.onAbout
    )
    SettingsRow(
        icon = R.drawable.ic_row_logout,
        title = stringResource(R.string.profile_logout_title),
        danger = true,
        onClick = onSignOut
    )
}

