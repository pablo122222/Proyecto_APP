package me.pablorincon.proyecto_app.ui.screens.lab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.glitchcity.LabRun
import me.pablorincon.proyecto_app.domain.glitchcity.LineNote
import me.pablorincon.proyecto_app.domain.glitchcity.LineStatus
import me.pablorincon.proyecto_app.ui.screens.lesson.CodeBlock
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.CityNodeDone
import me.pablorincon.proyecto_app.ui.theme.ErrorRed
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Primary
import me.pablorincon.proyecto_app.ui.theme.SoftBlueCardBg

private val ExplanationShape = RoundedCornerShape(12.dp)

// Código del laboratorio con su camino marcado (verde: se ejecuta, rojo: condición falsa,
// atenuada: no se ejecuta). Al tocar una línea se explica qué hace con los valores actuales.
@Composable
internal fun TracedCode(run: LabRun) {
    var selected by rememberSaveable { mutableStateOf<Int?>(null) }
    val backgrounds = run.trace.mapIndexedNotNull { index, line ->
        when {
            index == selected -> index to Primary.copy(alpha = 0.5f)
            line.status == LineStatus.Ran -> index to CityNodeDone.copy(alpha = 0.25f)
            line.status == LineStatus.False -> index to ErrorRed.copy(alpha = 0.35f)
            else -> null
        }
    }.toMap()
    val dimmed = run.trace.indices.filter { run.trace[it].status == LineStatus.Skipped }.toSet()

    CodeBlock(
        code = run.code.joinToString("\n"),
        onLineClick = { selected = if (selected == it) null else it },
        lineBackgrounds = backgrounds,
        dimmedLines = dimmed
    )
    Spacer(Modifier.height(6.dp))
    Text(stringResource(R.string.code_legend), style = AppTextStyles.RowSubtitle, color = Muted)
    Spacer(Modifier.height(10.dp))

    val line = selected?.let { run.trace.getOrNull(it) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ExplanationShape)
            .background(SoftBlueCardBg)
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(12.dp)
    ) {
        if (line == null) {
            Text(stringResource(R.string.code_tap_hint), style = AppTextStyles.SmallBody, color = Muted)
        } else {
            Text(stringResource(R.string.code_line_title, selected!! + 1), style = AppTextStyles.RowTitle, color = Ink)
            Spacer(Modifier.height(4.dp))
            Text(noteText(line.note), style = AppTextStyles.SmallBody, color = Ink)
        }
    }
}

@Composable
private fun noteText(note: LineNote): String {
    val none = stringResource(R.string.code_none)
    fun List<Int>.orNone() = if (isEmpty()) none else joinToString(", ")
    return when (note) {
        is LineNote.Assign -> stringResource(R.string.note_assign, note.name, note.value)
        is LineNote.Check -> stringResource(if (note.holds) R.string.note_check_true else R.string.note_check_false, note.evaluated)
        LineNote.NotReached -> stringResource(R.string.note_not_reached)
        LineNote.ElseRuns -> stringResource(R.string.note_else)
        LineNote.InsideFalseIf -> stringResource(R.string.note_inside_false_if)
        is LineNote.Loop -> pluralStringResource(
            R.plurals.note_loop, note.values.size, note.values.size, note.variable, note.values.joinToString(", ")
        )
        is LineNote.LoopBody -> pluralStringResource(R.plurals.note_loop_body, note.times, note.times)
        is LineNote.ListValue ->
            if (note.count == 0) stringResource(R.string.note_list_empty, note.name)
            else pluralStringResource(R.plurals.note_list, note.count, note.name, note.count)
        is LineNote.Call ->
            if (note.result == null) stringResource(R.string.note_call_empty, note.function)
            else stringResource(R.string.note_call, note.function, note.result)
        is LineNote.Define -> stringResource(R.string.note_define, note.function, note.uses.joinToString(", "))
        is LineNote.ShopCheck -> stringResource(R.string.note_shop_check, note.minimum, note.yes.orNone(), note.no.orNone())
        is LineNote.ReturnDiscount ->
            if (note.results.isEmpty()) stringResource(R.string.note_return_discount_unused)
            else stringResource(R.string.note_return_discount, note.results.joinToString(", ") { (buy, pays) -> "$buy → $pays" })
        is LineNote.ReturnPlain ->
            if (note.purchases.isEmpty()) stringResource(R.string.note_return_plain_unused)
            else stringResource(R.string.note_return_plain, note.purchases.joinToString(", "))
    }
}
