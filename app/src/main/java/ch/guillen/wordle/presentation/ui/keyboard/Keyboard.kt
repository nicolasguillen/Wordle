package ch.guillen.wordle.presentation.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.guillen.wordle.presentation.theme.HitColors
import ch.guillen.wordle.presentation.theme.WordleTheme
import ch.guillen.wordle.presentation.ui.CharStatus

@Composable
fun Keyboard(
    state: KeyboardState,
    modifier: Modifier = Modifier,
    onKeyPress: (KeyType) -> Unit = {  },
) {
    Surface(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
    ) {
        Column {
            state.rows.forEach { row ->
                Row {
                    row.forEach { model ->
                        Key(
                            modifier = Modifier
                                .weight(model.type.weight),
                            type = model.type,
                            color = model.color,
                            onKeyPress = onKeyPress
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Key(
    modifier: Modifier = Modifier,
    type: KeyType,
    color: Color,
    onKeyPress: (KeyType) -> Unit = {  }
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .padding(1.dp)
            .background(when(type) {
                KeyType.Space -> Color.Unspecified
                else -> color
            })
            .clickable { onKeyPress(type) },
        contentAlignment = Alignment.Center,
    ) {
        if(type is KeyType.Backspace) {
            Icon(
                imageVector = Icons.Filled.Backspace,
                contentDescription = "Delete"
            )
        } else {
            Text(
                text = type.label,
                color = MaterialTheme.colors.onBackground
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultKeyboard() {
    val state = KeyboardState().apply {
        updateLetter(KeyType.Letter('R'), CharStatus.ABSENT)
        updateLetter(KeyType.Letter('X'), CharStatus.CORRECT)
    }

    WordleTheme(darkTheme = false) {
        Keyboard(state)
    }
}