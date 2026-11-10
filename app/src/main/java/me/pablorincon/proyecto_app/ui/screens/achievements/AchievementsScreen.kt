package me.pablorincon.proyecto_app.ui.screens.achievements

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.AchievementCategory
import me.pablorincon.proyecto_app.domain.AchievementProgress
import me.pablorincon.proyecto_app.domain.Tier
import me.pablorincon.proyecto_app.ui.components.AchievementMedal
import me.pablorincon.proyecto_app.ui.components.AppCard
import me.pablorincon.proyecto_app.ui.components.BackButton
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.components.ProgressBar
import me.pablorincon.proyecto_app.ui.components.SectionHeading
import me.pablorincon.proyecto_app.ui.components.TierPill
import me.pablorincon.proyecto_app.ui.components.achievementDescription
import me.pablorincon.proyecto_app.ui.components.achievementName
import me.pablorincon.proyecto_app.ui.components.categoryName
import me.pablorincon.proyecto_app.ui.components.tierColor
import me.pablorincon.proyecto_app.ui.components.tierName
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.InterFamily
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.LockedPillBackground
import me.pablorincon.proyecto_app.ui.theme.LockedPillText
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.RadioBorder

private val CardShape = RoundedCornerShape(16.dp)

// Pantalla "Mis logros" (guía §6): resumen, logros por categoría con su nivel de dominio,
// y el detalle de cada uno al tocarlo.
@Composable
fun AchievementsScreen(
    onBack: () -> Unit,
    onKeepPracticing: () -> Unit,
    viewModel: AchievementsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(start = AppDimens.ScreenHorizontal, end = AppDimens.ScreenHorizontal, top = AppDimens.ScreenTop, bottom = AppDimens.ScreenBottom)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(AppDimens.TopLineGap))
            Text(
                text = stringResource(R.string.title_achievements),
                style = AppTextStyles.ScreenTitleSmall,
                color = Ink,
                modifier = Modifier.weight(1f)
            )
            if (state is AchievementsUiState.Ready) {
                val ready = state as AchievementsUiState.Ready
                Pill(
                    text = stringResource(R.string.achievements_pill, ready.unlocked, ready.items.size),
                    containerColor = Mint,
                    contentColor = MintInk
                )
            }
        }
        Spacer(Modifier.height(AppDimens.TopLineBottom))
        when (val current = state) {
            AchievementsUiState.Loading -> Box(Modifier.fillMaxWidth().padding(top = 64.dp), Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
            AchievementsUiState.Error -> {
                Text(
                    text = stringResource(R.string.home_error),
                    style = AppTextStyles.SmallBody,
                    color = Muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(AppDimens.ErrorToButton))
                PrimaryButton(text = stringResource(R.string.startup_retry), onClick = viewModel::load)
            }
            is AchievementsUiState.Ready -> AchievementsContent(current, onSelect = viewModel::select, onKeepPracticing = onKeepPracticing)
        }
    }
    (state as? AchievementsUiState.Ready)?.selected?.let { selected ->
        AchievementDetailSheet(selected, onDismiss = { viewModel.select(null) })
    }
}

@Composable
private fun AchievementsContent(
    state: AchievementsUiState.Ready,
    onSelect: (AchievementProgress) -> Unit,
    onKeepPracticing: () -> Unit
) {
    Text(text = stringResource(R.string.achievements_sub), style = AppTextStyles.Subtitle, color = Muted)
    Spacer(Modifier.height(16.dp))
    // Resumen de dominio y gemas ganadas
    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.achievements_summary, state.tiersReached, state.tiersTotal),
                    style = AppTextStyles.RecommendTitle.copy(fontSize = 13.sp),
                    color = Ink
                )
                Spacer(Modifier.height(4.dp))
                Text(text = stringResource(R.string.achievements_summary_text), style = AppTextStyles.RowSubtitle, color = Muted)
            }
            Spacer(Modifier.width(12.dp))
            Pill(text = stringResource(R.string.gems_pill, state.gems))
        }
        Spacer(Modifier.height(12.dp))
        ProgressBar(percent = if (state.tiersTotal == 0) 0 else state.tiersReached * 100 / state.tiersTotal)
    }
    if (state.unlocked == 0) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.achievements_empty),
            style = AppTextStyles.RowSubtitle,
            color = Muted,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, RadioBorder, RoundedCornerShape(14.dp))
                .background(AppBackground)
                .padding(14.dp)
        )
    }

    // Por categoría
    AchievementCategory.entries.forEach { category ->
        val items = state.items.filter { it.achievement.category == category }
        if (items.isNotEmpty()) AchievementGrid(stringResource(categoryName(category)), items, onSelect)
    }
    Spacer(Modifier.height(12.dp))
    PrimaryButton(text = stringResource(R.string.achievements_keep_practicing), onClick = onKeepPracticing)
}

@Composable
private fun AchievementGrid(title: String, items: List<AchievementProgress>, onSelect: (AchievementProgress) -> Unit) {
    SectionHeading(title = title)
    items.chunked(2).forEach { pair ->
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            pair.forEach { item -> AchievementCard(item, Modifier.weight(1f)) { onSelect(item) } }
            if (pair.size == 1) Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
    }
}

// Tarjeta de un logro: medalla, nombre, descripción, barra hacia el siguiente nivel y estado.
@Composable
private fun AchievementCard(item: AchievementProgress, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val a = item.achievement
    val locked = item.tiersReached == 0
    Column(
        modifier = modifier
            .clip(CardShape)
            .background(Paper)
            .border(if (locked) 1.dp else 1.5.dp, if (locked) Line else tierColor(item.currentTier), CardShape)
            .clickable(role = Role.Button, onClick = onClick)
            .alpha(if (locked) 0.78f else 1f) // Los bloqueados al 78 % (guía §6)
            .padding(12.dp)
    ) {
        AchievementMedal(emoji = a.emoji, tier = item.currentTier)
        Spacer(Modifier.height(8.dp))
        Text(text = stringResource(achievementName(a.id)), style = AppTextStyles.RecommendTitle.copy(fontSize = 11.sp), color = Ink)
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(achievementDescription(a.id)),
            style = TextStyle(fontFamily = InterFamily, fontSize = 9.sp, lineHeight = 12.sp),
            color = Muted,
            maxLines = 3
        )
        Spacer(Modifier.height(8.dp))
        ProgressBar(percent = (item.fraction * 100).toInt())
        Spacer(Modifier.height(5.dp))
        Text(
            text = progressText(item),
            style = TextStyle(fontFamily = InterFamily, fontSize = 9.sp),
            color = Muted
        )
        Spacer(Modifier.height(6.dp))
        val current = item.currentTier
        if (current == null) {
            Pill(text = stringResource(R.string.achievements_locked), containerColor = LockedPillBackground, contentColor = LockedPillText)
        } else {
            Pill(
                text = stringResource(R.string.achievements_tier_count, stringResource(tierName(current)), item.tiersReached, a.tierCount),
                containerColor = Mint,
                contentColor = MintInk
            )
        }
        // Guía §6: gemas del siguiente nivel, u "obtenida" cuando ya no quedan niveles
        Spacer(Modifier.height(6.dp))
        val nextGems = item.nextGems
        Text(
            text = if (nextGems != null) stringResource(R.string.achievements_card_reward, nextGems)
            else stringResource(R.string.achievements_card_reward_obtained),
            style = TextStyle(fontFamily = InterFamily, fontSize = 9.sp, fontWeight = FontWeight.SemiBold),
            color = if (nextGems != null) Primary else MintInk
        )
    }
}

@Composable
private fun progressText(item: AchievementProgress): String {
    val next = item.nextTier ?: return stringResource(R.string.achievements_max_level)
    return stringResource(R.string.achievements_next, item.value.coerceAtMost(item.nextThreshold ?: 0), item.nextThreshold ?: 0, stringResource(tierName(next)))
}

// Detalle de un logro: sus niveles, qué pide cada uno, sus gemas y si ya se obtuvo.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AchievementDetailSheet(item: AchievementProgress, onDismiss: () -> Unit) {
    val a = item.achievement
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Paper,
        scrimColor = Color(0x731F2A44),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            Modifier.padding(start = 22.dp, end = 22.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AchievementMedal(emoji = a.emoji, tier = item.currentTier, size = 72)
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(achievementName(a.id)),
                style = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight(800), fontSize = 18.sp),
                color = Ink
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(achievementDescription(a.id)),
                style = AppTextStyles.SmallBody,
                color = Muted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            a.thresholds.indices.forEach { index ->
                val tier = Tier.entries[index]
                val reached = index < item.tiersReached
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (reached) Mint else AppBackground)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TierPill(tier)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.achievements_tier_goal, a.thresholds[index]),
                        style = AppTextStyles.RowSubtitle,
                        color = Ink,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (reached) stringResource(R.string.achievements_tier_obtained)
                        else stringResource(R.string.gems_reward, a.gems[index]),
                        style = AppTextStyles.Pill,
                        color = if (reached) MintInk else Primary
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.achievements_current_value, item.value),
                style = AppTextStyles.RowSubtitle,
                color = Muted
            )
            if (item.gemsEarned > 0) {
                Spacer(Modifier.height(4.dp))
                Box(Modifier.clip(CircleShape)) {
                    Text(text = stringResource(R.string.achievements_gems_earned, item.gemsEarned), style = AppTextStyles.RowSubtitle, color = Primary)
                }
            }
        }
    }
}
