import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

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
                Column(modifier = Modifier.weight(1f)) {
                    teams.forEach { team ->
                        TeamControls(team, modifier = Modifier.fillMaxWidth(1f)) { newScore ->
                            onScoreChanged(team, newScore)
                        }
                    }
                    NewPlayerInput(onNewTeamAdded)
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
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(team.name)
        Button(
            modifier = Modifier.padding(start = 20.dp),
            onClick = {
                onTeamScoreUpdate(team.score - 1)
            }) {
            Text("-")
        }
        Button(
            modifier = Modifier.padding(start = 10.dp, end = 20.dp),
            onClick = {
                onTeamScoreUpdate(team.score + 1)
            }) {
            Text("+")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NewPlayerInput(onNewTeamAdded: (teamName: String) -> Unit) {
    val textState = remember { mutableStateOf(String()) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
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
            modifier = Modifier.padding(horizontal = 20.dp),
            onClick = {
                submitNewPlayer()
            }) {
            Text("Voeg toe")
        }
    }
}

@Preview
@Composable
fun InputScreenPreview() {
    val testTeams = listOf(Team("Team 1", 0), Team("Team 2", 2))
    InputScreen(testTeams, remember { mutableStateOf(true) }, { _, _ -> }, {})
}