package me.pablorincon.proyecto_app.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics

// Emojis y símbolos de adorno ("›", "✦", el emoji de una tarjeta): TalkBack los salta,
// porque el texto que tienen al lado ya dice lo mismo y leerlos solo agrega ruido
// ("comilla angular", "semáforo"...).
fun Modifier.decorative(): Modifier = clearAndSetSemantics { }
