package me.pablorincon.proyecto_app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.pablorincon.proyecto_app.ui.theme.AppTextStyles
import me.pablorincon.proyecto_app.ui.theme.Ink
import me.pablorincon.proyecto_app.ui.theme.InterFamily
import me.pablorincon.proyecto_app.ui.theme.Muted
import me.pablorincon.proyecto_app.ui.theme.Paper

// Hoja inferior de confirmación (sheet de la guía §0): fondo oscuro al 45 %, panel blanco con
// esquinas superiores de 24, título de 18 sp, texto de 12 sp y botones apilados.
// Se cierra tocando fuera o con atrás.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmSheet(
    title: String,
    message: String,
    confirmLabel: String,
    cancelLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = Paper,
        scrimColor = Color(0x731F2A44), // #1F2A44 al 45 %
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 6.dp, bottom = 30.dp)) {
            Text(
                text = title,
                style = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp),
                color = Ink
            )
            Spacer(Modifier.height(6.dp))
            Text(text = message, style = AppTextStyles.SmallBody, color = Muted)
            Spacer(Modifier.height(18.dp))
            PrimaryButton(text = confirmLabel, onClick = onConfirm)
            Spacer(Modifier.height(10.dp))
            SecondaryButton(text = cancelLabel, onClick = onDismiss, modifier = Modifier.fillMaxWidth())
        }
    }
}
