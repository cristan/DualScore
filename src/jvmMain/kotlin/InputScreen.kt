import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputScreen(
    teams: List<Team>,
    darkModeState: MutableState<Boolean>,
    onScoreChanged: (team: Team, newScore: Int) -> Unit,
    onNewTeamAdded: (teamName: String) -> Unit
) {
    MaterialTheme(colors = if (darkModeState.value) darkColors() else lightColors()) {
        Scaffold {
            Row {
                Column(modifier = Modifier.weight(1f).padding(end = 20.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            teams.forEach { team ->
                                TeamControls(team) { newScore ->
                                    onScoreChanged(team, newScore)
                                }
                            }
                            NewPlayerInput(onNewTeamAdded)
                        }
                    }
                }
                // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
                Column(Modifier.selectableGroup()) {
                    DarkModeSelector(darkModeState)
                }
            }
        }
    }
}

@Composable
private fun DarkModeSelector(darkModeState: MutableState<Boolean>) {
    Row {
        Text("Modus", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
    }
    val modes = listOf("Donker", "Licht")
    modes.forEach { text ->
        Row(
            Modifier
                .height(56.dp)
                .padding(end = 8.dp)
                .selectable(
                    selected = if (text == modes[0]) darkModeState.value else !darkModeState.value,
                    onClick = { darkModeState.value = text == modes[0] },
                    role = Role.RadioButton
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = if (text == modes[0]) darkModeState.value else !darkModeState.value,
                onClick = null // null recommended for accessibility with screenreaders
            )
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun TeamControls(
    team: Team,
    modifier: Modifier = Modifier,
    onTeamScoreUpdate: (newScore: Int) -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = team.name,
            style = MaterialTheme.typography.h5
        )
        Button(
            modifier = Modifier.padding(start = 10.dp),
            onClick = {
                onTeamScoreUpdate(team.score - 1)
            }) {
            Image(
                painter = painterResource("images/remove.svg"),
                contentDescription = "Verlaag score",
                modifier = Modifier.size(24.dp)
            )
        }
        TextField(
            value = team.score.toString(),
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { onTeamScoreUpdate(it) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(68.dp).padding(start = 10.dp),
        )

        Button(
            modifier = Modifier.padding(start = 10.dp),
            onClick = {
                onTeamScoreUpdate(team.score + 1)
            }) {
            Image(
                painter = painterResource("images/add.svg"),
                contentDescription = "Verhoog score",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NewPlayerInput(onNewTeamAdded: (teamName: String) -> Unit) {
    val textState = remember { mutableStateOf(String()) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val submitNewPlayer = {
            if (textState.value.isNotEmpty()) {
                onNewTeamAdded(textState.value)
                textState.value = ""
            }
        }

        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            maxLines = 1,
            label = {
                Text("Nieuw team")
            },
            modifier = Modifier.onKeyEvent {
                if (it.key == Key.Enter && it.type == KeyEventType.KeyUp) {
                    submitNewPlayer()
                    return@onKeyEvent true
                }
                false
            }
        )
        Button(
            modifier = Modifier.padding(start = 20.dp),
            onClick = {
                submitNewPlayer()
            }) {
            Text(
                text = "Voeg toe",
                fontSize = 16.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview
@Composable
fun InputScreenPreview() {
    val testTeams = listOf(
        Team("Armchair Athletes", 0),
        Team("The Mighty Morphin Flower Arrangers", 2),
        Team("Victorious Secret", 1),
    )
    InputScreen(testTeams, remember { mutableStateOf(true) }, { _, _ -> }, {})
}