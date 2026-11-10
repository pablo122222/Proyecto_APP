package me.pablorincon.proyecto_app.ui.screens.glitchcity

import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.UserProfile
import me.pablorincon.proyecto_app.domain.glitchcity.DroneLevel
import me.pablorincon.proyecto_app.domain.glitchcity.DroneLevelStatus
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityCatalog
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityProgress
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchCityState
import me.pablorincon.proyecto_app.domain.glitchcity.GlitchTitles
import me.pablorincon.proyecto_app.ui.components.AppCard
import me.pablorincon.proyecto_app.ui.components.ConfirmSheet
import me.pablorincon.proyecto_app.ui.components.Pill
import me.pablorincon.proyecto_app.ui.components.PrimaryButton
import me.pablorincon.proyecto_app.ui.theme.AccentYellow
import me.pablorincon.proyecto_app.ui.theme.AppAppearance
import me.pablorincon.proyecto_app.ui.theme.AppBackground
import me.pablorincon.proyecto_app.ui.theme.AppDimens
import me.pablorincon.proyecto_app.ui.theme.AppShapes
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CityNodeDone
import me.pablorincon.proyecto_app.ui.theme.CodeMentorTheme
import me.pablorincon.proyecto_app.ui.theme.HeroEnd
import me.pablorincon.proyecto_app.ui.theme.HeroStart
import me.pablorincon.proyecto_app.ui.theme.HeroText
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Line
import me.pablorincon.proyecto_app.ui.theme.LockedPillBackground
import me.pablorincon.proyecto_app.ui.theme.LockedPillText
import me.pablorincon.proyecto_app.ui.theme.Mint
import me.pablorincon.proyecto_app.ui.theme.MintInk
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.NodeDoneShadow
import me.pablorincon.proyecto_app.ui.theme.NodeLockedFill
import me.pablorincon.proyecto_app.ui.theme.NodeLockedShadow
import me.pablorincon.proyecto_app.ui.theme.NodeLockedText
import me.pablorincon.proyecto_app.ui.theme.Paper
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.PrimaryDark
import me.pablorincon.proyecto_app.ui.theme.PrimarySoft
import me.pablorincon.proyecto_app.ui.theme.RowIconBackground
import me.pablorincon.proyecto_app.ui.theme.Yellow
import me.pablorincon.proyecto_app.ui.theme.YellowInk

// Pantalla "Glitch City" (pestaña Retos, guía §5.1): cabecera con gemas, racha y XP, el título
// equipado y las pestañas Explorar, Crear y Competir. Todo sale del progreso real del usuario.
@Composable
fun GlitchCityScreen(
    onOpenActivity: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GlitchCityViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    // Al volver de una actividad u otra pestaña se actualizan los números
    LifecycleResumeEffect(viewModel) {
        viewModel.load(silent = true)
        onPauseOrDispose { }
    }
    GlitchCityContent(
        state = state,
        actions = CityActions(
            onSelectTab = viewModel::selectTab,
            onOpenActivity = onOpenActivity,
            onEquipTitle = viewModel::equipTitle,
            onOpenReset = viewModel::openResetSheet,
            onRetry = viewModel::load
        ),
        modifier = modifier
    )
    if (state.showResetSheet) {
        ConfirmSheet(
            title = stringResource(R.string.city_reset_title),
            message = stringResource(R.string.city_reset_message),
            confirmLabel = stringResource(R.string.city_reset_yes),
            cancelLabel = stringResource(R.string.city_reset_close),
            onConfirm = viewModel::reset,
            onDismiss = viewModel::closeResetSheet
        )
    }
    // Avisos cortos (toast de la guía §0)
    val toastText = when (state.message) {
        CityMessage.Reset -> stringResource(R.string.city_reset_done)
        CityMessage.NoConnection -> stringResource(R.string.settings_no_connection)
        CityMessage.Failed -> stringResource(R.string.settings_failed)
        null -> null
    }
    LaunchedEffect(toastText) {
        if (toastText != null) {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }
}

// Lo que el usuario puede hacer en la pantalla.
private class CityActions(
    val onSelectTab: (CityTab) -> Unit,
    val onOpenActivity: (String) -> Unit,
    val onEquipTitle: (String) -> Unit,
    val onOpenReset: () -> Unit,
    val onRetry: () -> Unit
)

// Solo dibuja: avisa de lo que hace el usuario.
@Composable
private fun GlitchCityContent(
    state: GlitchCityUiState,
    actions: CityActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when (val content = state.content) {
            CityContent.Loading -> {
                val loading = stringResource(R.string.city_loading)
                Box(Modifier.fillMaxWidth().padding(top = 96.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.semantics { contentDescription = loading })
                }
            }
            CityContent.Error -> Column(Modifier.padding(horizontal = AppDimens.ScreenHorizontal)) {
                Text(
                    text = stringResource(R.string.city_error),
                    style = AppTextStyles.SmallBody,
                    color = Muted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 96.dp)
                )
                Spacer(Modifier.height(AppDimens.ErrorToButton))
                PrimaryButton(text = stringResource(R.string.startup_retry), onClick = actions.onRetry)
            }
            is CityContent.Ready -> {
                // La cabecera queda fija arriba; el resto se desplaza
                Header(gems = content.city.profile.gems, streak = content.streak, xp = content.city.profile.xp)
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = AppDimens.ScreenHorizontal,
                            end = AppDimens.ScreenHorizontal,
                            bottom = AppDimens.ScreenBottom // Libre para la barra de pestañas
                        )
                ) {
                    Banner(titleId = content.city.shownTitleId)
                    Spacer(Modifier.height(12.dp))
                    TabRow(selected = state.tab, onSelect = actions.onSelectTab)
                    Spacer(Modifier.height(16.dp))
                    val progress = content.city.progress
                    when (state.tab) {
                        CityTab.Explore -> ExploreTab(progress, actions.onOpenActivity)
                        CityTab.Create -> CreateTab(progress, actions.onOpenActivity)
                        CityTab.Compete -> CompeteTab(progress, actions.onOpenActivity)
                        CityTab.Titles -> TitlesTab(progress, content.city.shownTitleId, actions.onEquipTitle)
                    }
                    ResetLink(onClick = actions.onOpenReset)
                }
            }
        }
    }
}

// Pills "◆ gemas" (azul), "🔥 racha" (amarillo) y "XP" (mint).
@Composable
private fun Header(gems: Int, streak: Int, xp: Int) {
    val gemsDescription = pluralStringResource(R.plurals.gems_description, gems, gems)
    val streakDescription = stringResource(
        R.string.home_streak_description,
        pluralStringResource(R.plurals.home_streak, streak, streak)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground)
            .padding(horizontal = AppDimens.ScreenHorizontal, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Pill(
            text = stringResource(R.string.city_gems, gems),
            modifier = Modifier.semantics { contentDescription = gemsDescription }
        )
        Pill(
            text = stringResource(R.string.city_streak, streak),
            containerColor = Yellow,
            contentColor = YellowInk,
            modifier = Modifier.semantics { contentDescription = streakDescription }
        )
        Pill(text = stringResource(R.string.city_xp, xp), containerColor = Mint, contentColor = MintInk)
    }
}

private val BannerShape = RoundedCornerShape(16.dp)

// "Glitch City" + "Tu título: {título equipado}" sobre el degradado hero.
@Composable
private fun Banner(titleId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(BannerShape)
            .background(Brush.horizontalGradient(listOf(HeroStart, HeroEnd)))
            .padding(horizontal = 14.dp, vertical = 11.dp)
    ) {
        Text(
            text = stringResource(R.string.title_glitch_city),
            style = AppTextStyles.RowTitle.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(R.string.city_banner_title, stringResource(GlitchCityText.titleName(titleId))),
            style = AppTextStyles.RowSubtitle.copy(fontSize = 10.sp),
            color = HeroText
        )
    }
}

// Fila de botones: el activo en azul con texto blanco.
@Composable
private fun TabRow(selected: CityTab, onSelect: (CityTab) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CityTab.entries.forEach { tab ->
            val active = tab == selected
            Text(
                text = stringResource(
                    when (tab) {
                        CityTab.Explore -> R.string.city_tab_explore
                        CityTab.Create -> R.string.city_tab_create
                        CityTab.Compete -> R.string.city_tab_compete
                        CityTab.Titles -> R.string.city_tab_titles
                    }
                ),
                style = AppTextStyles.SmallButton.copy(fontSize = 11.sp),
                color = if (active) Color.White else Primary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .clip(AppShapes.SmallButton)
                    .background(if (active) Primary else PrimarySoft)
                    .semantics { this.selected = active }
                    .clickable(role = Role.Tab) { onSelect(tab) }
                    .padding(vertical = 10.dp, horizontal = 4.dp) // Poco margen lateral: caben 4 botones
            )
        }
    }
}

@Composable
private fun Intro(text: String) {
    Text(text = text, style = AppTextStyles.Subtitle, color = Muted)
    Spacer(Modifier.height(14.dp))
}

// ---------- Explorar y Crear: tarjetas por actividad ----------

@Composable
private fun ExploreTab(progress: GlitchCityProgress, onOpenActivity: (String) -> Unit) {
    Intro(stringResource(R.string.city_explore_intro))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        GlitchCityCatalog.labs.forEach { lab ->
            val (name, topic) = GlitchCityText.activityNames.getValue(lab.id)
            ActivityCard(
                emoji = lab.emoji,
                title = stringResource(name),
                subtitle = stringResource(
                    R.string.city_lab_progress,
                    stringResource(topic),
                    progress.discoveriesIn(lab),
                    lab.discoveryIds.size
                ),
                action = stringResource(R.string.city_enter),
                onClick = { onOpenActivity(lab.id) }
            )
        }
    }
}

@Composable
private fun CreateTab(progress: GlitchCityProgress, onOpenActivity: (String) -> Unit) {
    Intro(stringResource(R.string.city_create_intro))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        GlitchCityCatalog.creations.forEach { creation ->
            val (name, topic) = GlitchCityText.activityNames.getValue(creation.id)
            val published = progress.isPublished(creation)
            ActivityCard(
                emoji = creation.emoji,
                title = stringResource(name),
                subtitle = stringResource(
                    if (published) R.string.city_creation_published else R.string.city_creation_unpublished,
                    stringResource(topic)
                ),
                action = stringResource(if (published) R.string.city_edit else R.string.city_create),
                onClick = { onOpenActivity(creation.id) }
            )
        }
    }
}

private val CardIconShape = RoundedCornerShape(12.dp)

// Tarjeta de una actividad: emoji, nombre, tema/estado y un botón a la derecha.
@Composable
private fun ActivityCard(emoji: String, title: String, subtitle: String, action: String, onClick: () -> Unit) {
    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(40.dp).clip(CardIconShape).background(RowIconBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, style = AppTextStyles.RowIcon)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = AppTextStyles.RowTitle, color = Ink)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = AppTextStyles.RowSubtitle, color = Muted)
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = action,
                style = AppTextStyles.SmallButton,
                color = Color.White,
                modifier = Modifier
                    .clip(AppShapes.SmallButton)
                    .background(Primary)
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(horizontal = 14.dp, vertical = 9.dp)
            )
        }
    }
}

// ---------- Competir: camino de niveles del dron ----------

// Zigzag de la guía: desplazamiento horizontal (dp) de cada nivel.
private val DroneZigzag = listOf(0, 46, 70, 46, 0)
private val DroneNodeSize = 66.dp
private val DroneNodeDepth = 6.dp   // Sombra inferior sólida
private val SpecialNodeShape = RoundedCornerShape(22.dp)

@Composable
private fun CompeteTab(progress: GlitchCityProgress, onOpenActivity: (String) -> Unit) {
    val context = LocalContext.current
    val lockedMessage = stringResource(R.string.city_drone_locked_toast)
    Intro(stringResource(R.string.city_compete_intro))
    GlitchCityCatalog.droneLevels.forEachIndexed { index, level ->
        val status = progress.status(index)
        DroneNode(
            level = level,
            number = index + 1,
            status = status,
            stars = progress.stars(level),
            record = progress.record(level),
            horizontalOffset = DroneZigzag[index % DroneZigzag.size].dp,
            onClick = {
                if (status == DroneLevelStatus.Locked) Toast.makeText(context, lockedMessage, Toast.LENGTH_SHORT).show()
                else onOpenActivity(level.id)
            }
        )
    }
}

// Un nivel: nodo de 66 con sombra sólida. Hecho: verde con ✓ · Actual: azul que late, con
// "EMPEZAR" encima · Bloqueado: gris. Los especiales son cuadrados redondeados y ámbar.
@Composable
private fun DroneNode(
    level: DroneLevel,
    number: Int,
    status: DroneLevelStatus,
    stars: Int?,
    record: Int?,
    horizontalOffset: Dp,
    onClick: () -> Unit
) {
    val shape: Shape = if (level.special) SpecialNodeShape else CircleShape
    val (fill, shadow) = when {
        status == DroneLevelStatus.Locked -> NodeLockedFill to NodeLockedShadow
        level.special -> AccentYellow to YellowInk
        status == DroneLevelStatus.Done -> CityNodeDone to NodeDoneShadow
        else -> Primary to PrimaryDark
    }
    val name = stringResource(GlitchCityText.droneLevelNames.getValue(level.id))
    val stateText = stringResource(
        when (status) {
            DroneLevelStatus.Done -> R.string.city_node_done
            DroneLevelStatus.Current -> R.string.city_node_current
            DroneLevelStatus.Locked -> R.string.city_node_locked
        }
    )
    val description = stringResource(R.string.city_node_description, number, name, stateText)

    // "Late" el nivel actual, salvo con "Reducir animaciones"
    val pulse = if (status == DroneLevelStatus.Current && !AppAppearance.reduceMotion) {
        val transition = rememberInfiniteTransition(label = "pulse")
        val value by transition.animateFloat(
            initialValue = 1f,
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
            label = "scale"
        )
        value
    } else {
        1f
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (status == DroneLevelStatus.Current) {
            Pill(
                text = stringResource(R.string.node_start).uppercase(),
                containerColor = Paper,
                contentColor = if (level.special) YellowInk else fill, // Ámbar sobre blanco se lee mal
                modifier = Modifier
                    .offset(x = horizontalOffset)
                    .border(AppDimens.BorderWidth, fill, CircleShape)
            )
            Spacer(Modifier.height(6.dp))
        }
        Box(
            modifier = Modifier
                .offset(x = horizontalOffset)
                .scale(pulse)
                .size(DroneNodeSize, DroneNodeSize + DroneNodeDepth)
                .semantics(mergeDescendants = true) {
                    contentDescription = description
                    role = Role.Button
                }
                .clickable(
                    onClick = onClick,
                    indication = null, // El nodo ya cambia de color: sin ripple
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(Modifier.offset(y = DroneNodeDepth).size(DroneNodeSize).clip(shape).background(shadow))
            Box(
                Modifier.size(DroneNodeSize).clip(shape).background(fill),
                contentAlignment = Alignment.Center
            ) {
                val symbolColor = if (status == DroneLevelStatus.Locked) NodeLockedText else Color.White
                if (status == DroneLevelStatus.Done) {
                    Text("✓", color = symbolColor, fontSize = 26.sp, fontWeight = FontWeight.Black)
                } else {
                    Text(number.toString(), color = symbolColor, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = name,
            style = AppTextStyles.RowTitle,
            color = if (status == DroneLevelStatus.Locked) NodeLockedText else Ink,
            modifier = Modifier.offset(x = horizontalOffset)
        )
        if (record != null && stars != null) {
            val starsText = "★".repeat(stars) + "☆".repeat(MAX_STARS - stars)
            val starsDescription = stringResource(R.string.city_stars_description, stars)
            val recordText = pluralStringResource(R.plurals.city_record, record, record, starsText)
            Spacer(Modifier.height(2.dp))
            Text(
                text = recordText,
                style = AppTextStyles.RowSubtitle,
                color = Muted,
                modifier = Modifier
                    .offset(x = horizontalOffset)
                    .semantics { contentDescription = recordText.replace(starsText, starsDescription) }
            )
        }
    }
}

private const val MAX_STARS = 3

// ---------- Títulos ----------

@Composable
private fun TitlesTab(progress: GlitchCityProgress, equippedId: String, onEquip: (String) -> Unit) {
    val earned = GlitchTitles.earned(progress)
    Intro(stringResource(R.string.city_titles_intro))
    AppCard {
        GlitchTitles.all.forEachIndexed { index, title ->
            TitleRow(
                titleId = title.id,
                isEarned = title.id in earned,
                isEquipped = title.id == equippedId,
                showDivider = index < GlitchTitles.all.lastIndex,
                onEquip = { onEquip(title.id) }
            )
        }
    }
}

// Fila: nombre + "Ganado: / Cómo ganarlo:" y a la derecha "Equipado", "Equipar" o "Bloqueado".
@Composable
private fun TitleRow(titleId: String, isEarned: Boolean, isEquipped: Boolean, showDivider: Boolean, onEquip: () -> Unit) {
    val description = stringResource(GlitchCityText.titleDescriptions.getValue(titleId))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (showDivider) Modifier.drawBottomLine() else Modifier)
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(stringResource(GlitchCityText.titleName(titleId)), style = AppTextStyles.RowTitle, color = Ink)
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(if (isEarned) R.string.city_title_earned else R.string.city_title_how, description),
                style = AppTextStyles.RowSubtitle,
                color = Muted
            )
        }
        Spacer(Modifier.width(10.dp))
        when {
            isEquipped -> Pill(stringResource(R.string.city_title_equipped), containerColor = Mint, contentColor = MintInk)
            isEarned -> Pill(
                text = stringResource(R.string.city_title_equip),
                modifier = Modifier
                    .clip(AppShapes.Pill)
                    .clickable(role = Role.Button, onClick = onEquip)
            )
            else -> Pill(
                stringResource(R.string.city_title_locked),
                containerColor = LockedPillBackground,
                contentColor = LockedPillText
            )
        }
    }
}

// Separador inferior de la fila (la última no lo lleva).
private fun Modifier.drawBottomLine(): Modifier = drawBehind {
    val stroke = AppDimens.BorderWidth.toPx()
    drawLine(Line, Offset(0f, size.height - stroke / 2), Offset(size.width, size.height - stroke / 2), stroke)
}

// Enlace gris subrayado al final: abre la hoja de confirmación.
@Composable
private fun ResetLink(onClick: () -> Unit) {
    Spacer(Modifier.height(AppDimens.SectionTop))
    Text(
        text = stringResource(R.string.city_reset_link),
        style = AppTextStyles.RowSubtitle.copy(textDecoration = TextDecoration.Underline),
        color = Muted,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 10.dp)
    )
}

@Preview(showSystemUi = true)
@Composable
private fun GlitchCityPreview() {
    CodeMentorTheme {
        GlitchCityContent(
            state = GlitchCityUiState(
                tab = CityTab.Compete,
                content = CityContent.Ready(
                    GlitchCityState(
                        UserProfile.initial("Ana"),
                        GlitchCityProgress(droneRecords = mapOf("d1" to 3))
                    ),
                    streak = 0
                )
            ),
            actions = CityActions({}, {}, {}, {}, {})
        )
    }
}
