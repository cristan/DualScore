import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BeamerScreen(teams: List<Team>, darkMode: Boolean) {
    MaterialTheme(colors = if (darkMode) darkColors() else lightColors()) {
        Scaffold {
            if (teams.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Welkom!",
                        style = MaterialTheme.typography.h1
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    val sortedTeams = teams.sortedByDescending { it.score }
                    items(sortedTeams, key = { it.name }) {team ->
                        TeamScore(team, darkMode, Modifier.animateItemPlacement())
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamScore(team: Team, darkMode: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(50.dp),
                )
                .padding(horizontal = 40.dp, vertical = 10.dp)
        ) {
            Text(
                text = team.name,
                color = if (darkMode) Color.Companion.Unspecified else MaterialTheme.colors.background,
                style = MaterialTheme.typography.h3
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 20.dp)
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(50.dp),
                )
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = team.score.toString(),
                color = if (darkMode) Color.Companion.Unspecified else MaterialTheme.colors.background,
                style = MaterialTheme.typography.h3
            )
        }
    }
}

@Preview
@Composable
fun BeamerScreenPreview() {
    val testTeams = listOf(Team("Team 1", 0), Team("Team 2", 2))
    BeamerScreen(testTeams, true)
}