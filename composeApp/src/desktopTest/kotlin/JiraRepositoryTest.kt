import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import kotlin.test.Ignore
import kotlin.test.Test

// TODO: this is an integration test that calls the productive Jira instance. Insert a valid username and password
// TODO: that has access to project TH on Jira here.
// FIXME: It would be better to start a local Http server and inject the URL of the endpoint to JiraRepository. So
// FIXME: when running the test the local server is used, when running for real the actual Jira server is.
private const val jiraUsername = ""
private const val jiraPassword = ""

class JiraRepositoryTest {

    @Test
    @Ignore
    fun `asking for done tickets in Sprint 82_2 returns two tickets`() {
        val sut = JiraRepository()
        val result = runBlocking { sut.getTicketsForSprint("82.2", jiraUsername, jiraPassword) }
        assertEquals(11, result.size)
        assertEquals("TH-3668", result[0].key)
    }
}