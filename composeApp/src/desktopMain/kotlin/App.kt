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

@Composable
@Preview
fun App(appViewModel: AppViewModel) {
    MaterialTheme {
        val viewModel = remember { appViewModel }
        val sprintNameFocusRequester = remember { FocusRequester() }
        val jiraUsernameFocusRequester = remember { FocusRequester() }
        val jiraPasswordFocusRequester = remember { FocusRequester() }
        if (viewModel.openErrorDialog.value) {
            ErrorDialog(shouldShowDialog = viewModel.openErrorDialog, message = viewModel.errorMessage.value)
        }
        LaunchedEffect(Unit) {
            sprintNameFocusRequester.requestFocus()
        }

        Column(modifier = Modifier.offset(4.dp)) {
            Text("Enter the name of the Sprint as it is displayed in Jira (e.g. '82.2').")
            OutlinedTextField(
                value = viewModel.sprintName.value,
                onValueChange = { sprintName -> viewModel.sprintName.value = sprintName },
                label = { Text("Sprint name") },
                modifier = focusSwitcher(sprintNameFocusRequester, jiraUsernameFocusRequester)
            )
            OutlinedTextField(
                value = viewModel.jiraUsername.value,
                onValueChange = { jiraUsername -> viewModel.jiraUsername.value = jiraUsername },
                label = { Text("Username for Jira") },
                modifier = focusSwitcher(jiraUsernameFocusRequester, jiraPasswordFocusRequester)
            )
            OutlinedTextField(
                value = viewModel.jiraPassword.value,
                onValueChange = { jiraPassword -> viewModel.jiraPassword.value = jiraPassword },
                label = { Text("Password for Jira") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = focusSwitcher(jiraPasswordFocusRequester, sprintNameFocusRequester)
            )
            Button(onClick = {
                viewModel.calculateButtonClicked()
            }) {
                Text("calculate logged work for Sprint")
            }
            OutlinedTextField(
                value = viewModel.workDistribution.value,
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
                Button(onClick = { shouldShowDialog.value = false }) {
                    Text(text = "Well well")
                }
            }
        )
    }
}
