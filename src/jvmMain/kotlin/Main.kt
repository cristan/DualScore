import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    val playersState = remember { mutableStateOf(emptyList<Team>()) }
    val darkModeState = remember { mutableStateOf(true) }
    val closeAppConfirmationOpen = remember { mutableStateOf(false) }

    Window(
        title = "DualScore 2.0",
        onCloseRequest = {
            closeAppConfirmationOpen.value = true
        }
    ) {
        BeamerScreen(playersState.value, darkModeState.value)
    }

    Window(
        state = WindowState(
            position = WindowPosition(Alignment.BottomCenter),
            size = DpSize(800.dp, 400.dp)
        ),
        title = "DualScore - Spelers en score invullen",
        onCloseRequest = {
            closeAppConfirmationOpen.value = true
        }
    ) {
        InputScreen(playersState.value, darkModeState,
            onNewTeamAdded = { teamName ->
                playersState.value = playersState.value + Team(teamName, 0)
            },
            onScoreChanged = { team, newScore ->
                playersState.value = playersState.value.map {
                    if (it == team) {
                        Team(it.name, newScore)
                    } else {
                        it
                    }
                }
            }
        )

        if (closeAppConfirmationOpen.value) {
            CloseAppConfirmDialog(
                darkMode = darkModeState.value,
                onConfirm = ::exitApplication,
                onDismiss = {
                    closeAppConfirmationOpen.value = false
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CloseAppConfirmDialog(darkMode: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    MaterialTheme(colors = if (darkMode) darkColors() else lightColors()) {
        AlertDialog(
            modifier = Modifier.defaultMinSize(minWidth = 200.dp),
            onDismissRequest = {
                // Somehow also triggers when you click INSIDE the alert dialog.
                // Do nothing to prevent this
            },
            title = {
                Text(text = "App afsluiten?")
            },
            text = {
                Text("Wil je de app afsluiten?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text("Afsluiten")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                    }) {
                    Text("Annuleren")
                }
            }
        )
    }
}
