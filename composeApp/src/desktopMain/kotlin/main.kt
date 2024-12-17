import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val jiraUrl = "https://jira.gls-group.eu/rest/api/2/search"

    Window(
        onCloseRequest = ::exitApplication,
        title = "Work Distribution Calculator",
    ) {
        val worklogService = WorklogService(JiraRepository(jiraUrl), EpicRepository())
        App(appViewModel = AppViewModel(worklogService))
    }
}
