package ch.guillen.wordle.presentation.theme

import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF388E3C)
val LightSurface = Color(0xFFACACAC)
val DarkSurface = Color(0xFF242424)

// Hit colors
object HitColors {
    val Unknown = Color.DarkGray.copy(alpha = 0.5f)
    val Miss = Color.Transparent
    val Partial = Color(0xFFFBC02D)
    val Hit = Color(0xFF4CAF50)
}