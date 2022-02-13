package ch.guillen.wordle.presentation.ui.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


@Composable
fun ExitWarningDialog(
    onExitGame: () -> Unit
) {
    val openDialog = remember { mutableStateOf(false)  }

    BackHandler {
        openDialog.value = true
    }

    if(!openDialog.value) {
        return
    }
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = "Are you sure?")
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                    onExitGame()
                }) {
                Text("Exit")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openDialog.value = false
                }) {
                Text("Dismiss")
            }
        }
    )
}