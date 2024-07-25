import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

private val worklogService = WorklogService(JiraRepository(), EpicRepository())

@Composable
@Preview
fun App() {
    MaterialTheme {
        var sprintName by remember { mutableStateOf("") }
        val sprintNameFocusRequester = remember { FocusRequester() }
        var jiraUsername by remember { mutableStateOf("") }
        val jiraUsernameFocusRequester = remember { FocusRequester() }
        var jiraPassword by remember { mutableStateOf("") }
        val jiraPasswordFocusRequester = remember { FocusRequester() }
        var workDistribution by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        val openErrorDialog = remember { mutableStateOf(false) }
        if (openErrorDialog.value) {
            ErrorDialog(shouldShowDialog = openErrorDialog, message = errorMessage)
        }
        LaunchedEffect(Unit) {
            sprintNameFocusRequester.requestFocus()
        }

        Column(modifier = Modifier.offset(4.dp)) {
            Text("Enter the name of the Sprint as it is displayed in Jira (e.g. '82.2').")
            OutlinedTextField(
                value = sprintName,
                onValueChange = { sprintName = it },
                label = { Text("Sprint name") },
                modifier = focusSwitcher(sprintNameFocusRequester, jiraUsernameFocusRequester)
            )
            OutlinedTextField(
                value = jiraUsername,
                onValueChange = { jiraUsername = it },
                label = { Text("Username for Jira") },
                modifier = focusSwitcher(jiraUsernameFocusRequester, jiraPasswordFocusRequester)
            )
            OutlinedTextField(
                value = jiraPassword,
                onValueChange = { jiraPassword = it },
                label = { Text("Password for Jira") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = focusSwitcher(jiraPasswordFocusRequester, sprintNameFocusRequester)
            )
            Button(onClick = {
                try {
                    workDistribution = worklogService
                        .calculateTimeDistribution(sprintName, jiraUsername, jiraPassword).toString()
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Something went wrong."
                    openErrorDialog.value = true
                }
            }) {
                Text("calculate logged work for Sprint")
            }
            OutlinedTextField(
                value = workDistribution,
                onValueChange = {},
                label = { Text("distribution of work") },
                enabled = false
            )
        }
    }
}

private fun focusSwitcher(currentElement: FocusRequester, nextFocusElement: FocusRequester)
    = Modifier.focusRequester(currentElement).onPreviewKeyEvent { keyEvent ->
        if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Tab) {
            nextFocusElement.requestFocus()
            true
        } else {
            false
        }
    }

@Composable
fun ErrorDialog(shouldShowDialog: MutableState<Boolean>, message: String) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },
            title = { Text(text = "Something went wrong") },
            text = { Text(text = "There was an error while loading data:\n$message") },
            confirmButton = {
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                    }
                ) {
                    Text(
                        text = "Well well"
                    )
                }
            }
        )
    }
}
