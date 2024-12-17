import io.mockk.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppViewModelTest {

    private lateinit var worklogService: WorklogService
    private lateinit var sut: AppViewModel

    @BeforeTest
    fun setup() {
        worklogService = mockk()
        sut = AppViewModel(worklogService)
    }

    @Test
    fun `pressing button with correct input should calculate worklogs`() {
        sut.sprintName.value = "82.2"
        sut.jiraUsername.value = "Player1"
        sut.jiraPassword.value = "secret"
        every { worklogService.calculateTimeDistribution(any(), any(), any()) } returns mapOf(
            Pair(EpicCategory.MAINTENANCE, 20),
            Pair(EpicCategory.BUSINESS, 10),
            Pair(EpicCategory.TECHNICAL_IMPROVEMENT, 30)
        )

        sut.calculateButtonClicked()

        assertEquals("", sut.errorMessage.value)
        assertEquals("{MAINTENANCE=20, BUSINESS=10, TECHNICAL_IMPROVEMENT=30}", sut.workDistribution.value)
        verify { worklogService.calculateTimeDistribution("82.2", "Player1", "secret") }
        confirmVerified(worklogService)
    }

    @Test
    fun `an error in the worklogservice should open the error dialog`() {
        every { worklogService.calculateTimeDistribution(any(), any(), any()) } throws RuntimeException("Here be dragons")
        sut.sprintName.value = "82.2"
        sut.jiraUsername.value = "Player1"
        sut.jiraPassword.value = "secret"

        sut.calculateButtonClicked()

        assertEquals(true, sut.openErrorDialog.value)
        assertEquals("Here be dragons", sut.errorMessage.value)
        verify { worklogService.calculateTimeDistribution("82.2", "Player1", "secret") }
        confirmVerified(worklogService)
    }

}
