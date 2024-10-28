import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.slf4j.LoggerFactory

class AppViewModel(private val worklogService: WorklogService) : ViewModel() {
    val sprintName = mutableStateOf("")
    val jiraUsername = mutableStateOf("")
    val jiraPassword = mutableStateOf("")
    var workDistribution = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    val openErrorDialog = mutableStateOf(false)
    val log = LoggerFactory.getLogger(this.javaClass)

    fun calculateButtonClicked() {
        try {
            workDistribution.value = worklogService.calculateTimeDistribution(
                sprintName.value, jiraUsername.value, jiraPassword.value).toString()
        } catch (e: Exception) {
            log.error("Exception while getting Sprint info: "
                    + e.message + "\n"
                    + e.stackTraceToString())
            errorMessage.value = e.message ?: "Something went wrong."
            openErrorDialog.value = true
        }
    }

}
