package me.pablorincon.proyecto_app.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.AchievementCategory
import me.pablorincon.proyecto_app.domain.AchievementUnlock
import me.pablorincon.proyecto_app.domain.Tier
import me.pablorincon.proyecto_app.ui.theme.AppAppearance
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.InterFamily
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft

// ---------- Textos y colores de los logros (compartidos por Logros, Perfil y el aviso) ----------

@StringRes
fun achievementName(id: String): Int = when (id) {
    "explain_why" -> R.string.achievement_explain_why
    "autonomous" -> R.string.achievement_autonomous
    "strategist" -> R.string.achievement_strategist
    "bug_hunter" -> R.string.achievement_bug_hunter
    "edge_cases" -> R.string.achievement_edge_cases
    "cost_meter" -> R.string.achievement_cost_meter
    "learn_from_error" -> R.string.achievement_learn_from_error
    "level_up" -> R.string.achievement_level_up
    else -> R.string.achievement_habit
}

@StringRes
fun achievementDescription(id: String): Int = when (id) {
    "explain_why" -> R.string.achievement_explain_why_desc
    "autonomous" -> R.string.achievement_autonomous_desc
    "strategist" -> R.string.achievement_strategist_desc
    "bug_hunter" -> R.string.achievement_bug_hunter_desc
    "edge_cases" -> R.string.achievement_edge_cases_desc
    "cost_meter" -> R.string.achievement_cost_meter_desc
    "learn_from_error" -> R.string.achievement_learn_from_error_desc
    "level_up" -> R.string.achievement_level_up_desc
    else -> R.string.achievement_habit_desc
}

@StringRes
fun categoryName(category: AchievementCategory): Int = when (category) {
    AchievementCategory.Comprehension -> R.string.achievement_category_comprehension
    AchievementCategory.ProblemSolving -> R.string.achievement_category_problem_solving
    AchievementCategory.Algorithmic -> R.string.achievement_category_algorithmic
    AchievementCategory.Debugging -> R.string.achievement_category_debugging
    AchievementCategory.Consistency -> R.string.achievement_category_consistency
}

@StringRes
fun tierName(tier: Tier): Int = when (tier) {
    Tier.Bronze -> R.string.achievement_tier_bronze
    Tier.Silver -> R.string.achievement_tier_silver
    Tier.Gold -> R.string.achievement_tier_gold
}

// Colores de las medallas (no están en el mockup): bronce, plata y oro, con su fondo suave.
fun tierColor(tier: Tier?): Color = when (tier) {
    Tier.Bronze -> Color(0xFFC0773A)
    Tier.Silver -> Color(0xFF8C98AE)
    Tier.Gold -> Color(0xFFE0A21B)
    null -> Color(0xFFCBD3E6)
}

fun tierBackground(tier: Tier?): Color = when (tier) {
    Tier.Bronze -> Color(0xFFFBE9DA)
    Tier.Silver -> Color(0xFFEDF0F5)
    Tier.Gold -> Color(0xFFFDF0D2)       // Fondo de la medalla de la guía §6
    null -> Color(0xFFF1F3F8)
}

// Medalla redonda con el emoji del logro y el anillo del color de su nivel.
@Composable
fun AchievementMedal(emoji: String, tier: Tier?, modifier: Modifier = Modifier, size: Int = 44) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size / 3).dp))
            .background(tierBackground(tier))
            .border(2.dp, tierColor(tier), RoundedCornerShape((size / 3).dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = (size / 2).sp)
    }
}

// ---------- Aviso de logro desbloqueado ----------

// Ventana de celebración: aparece al terminar una lección o un jefe (o al abrir Inicio) cuando
// se gana un nivel de logro. Muestra la medalla, el nivel y las gemas recibidas.
@Composable
fun AchievementUnlockedDialog(
    unlocks: List<AchievementUnlock>,
    onSeeAchievements: () -> Unit,
    onDismiss: () -> Unit
) {
    if (unlocks.isEmpty()) return
    val main = unlocks.last() // El nivel más alto ganado se muestra en grande
    val gems = unlocks.sumOf { it.gems }
    val pop = remember { Animatable(if (AppAppearance.reduceMotion) 1f else 0.6f) }
    LaunchedEffect(Unit) {
        pop.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Paper)
                .semantics { liveRegion = LiveRegionMode.Polite }
                .padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.achievement_unlocked_eyebrow).uppercase(),
                style = AppTextStyles.Eyebrow,
                color = Primary
            )
            Spacer(Modifier.height(16.dp))
            AchievementMedal(
                emoji = main.achievement.emoji,
                tier = main.tier,
                size = 96,
                modifier = Modifier.scale(pop.value)
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(achievementName(main.achievementId)),
                style = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight(800), fontSize = 20.sp),
                color = Ink,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            TierPill(main.tier)
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(achievementDescription(main.achievementId)),
                style = AppTextStyles.SmallBody,
                color = Muted,
                textAlign = TextAlign.Center
            )
            if (unlocks.size > 1) {
                Spacer(Modifier.height(12.dp))
                unlocks.dropLast(1).forEach { other ->
                    Text(
                        text = stringResource(
                            R.string.achievement_unlocked_also,
                            stringResource(achievementName(other.achievementId)),
                            stringResource(tierName(other.tier))
                        ),
                        style = AppTextStyles.RowSubtitle,
                        color = Muted,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimarySoft)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.achievement_unlocked_gems, gems),
                    style = AppTextStyles.RecommendTitle,
                    color = Primary
                )
            }
            Spacer(Modifier.height(20.dp))
            PrimaryButton(text = stringResource(R.string.achievement_unlocked_see), onClick = onSeeAchievements)
            Spacer(Modifier.height(10.dp))
            SecondaryButton(text = stringResource(R.string.achievement_unlocked_continue), onClick = onDismiss)
        }
    }
}

@Composable
fun TierPill(tier: Tier, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(tierBackground(tier))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(tierColor(tier)))
        Spacer(Modifier.width(6.dp))
        Text(text = stringResource(tierName(tier)), style = AppTextStyles.Pill, color = Ink)
    }
}
