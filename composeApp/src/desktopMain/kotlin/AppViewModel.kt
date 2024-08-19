import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    val sprintName = mutableStateOf("")
    val jiraUsername = mutableStateOf("")
    val jiraPassword = mutableStateOf("")
    var workDistribution = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    val openErrorDialog = mutableStateOf(false)

    private val jiraUrl = "https://jira.gls-group.eu/rest/api/2/search"

    private val worklogService = WorklogService(JiraRepository(jiraUrl), EpicRepository())

    fun calculateButtonClicked() {
        try {
            workDistribution.value = worklogService.calculateTimeDistribution(
                sprintName.value, jiraUsername.value, jiraPassword.value).toString()
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "Something went wrong."
            openErrorDialog.value = true
        }
    }

}
