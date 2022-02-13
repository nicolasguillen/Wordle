package ch.guillen.wordle.presentation.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.guillen.wordle.R
import ch.guillen.wordle.presentation.theme.WordleTheme

@Composable
fun HomeScreen(
    startNewGame: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = startNewGame,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.primary
            ),
        ) {
            Text(
                text = stringResource(R.string.NewGame).uppercase()
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DarkHome() {
    WordleTheme(darkTheme = false) {
        HomeScreen()
    }
}