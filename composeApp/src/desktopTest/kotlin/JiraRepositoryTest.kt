import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.serialization.encodeToString

private const val jiraUsername = "user"
private const val jiraPassword = "pass"

class JiraRepositoryTest {

    private lateinit var mockWebServer: MockWebServer

    @BeforeTest
    fun setup() {
        mockWebServer = MockWebServer()
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `asking for done tickets in Sprint 82_2 returns two tickets`() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(Json.encodeToString(ticketList()))
        mockWebServer.enqueue(mockResponse)
        mockWebServer.start()

        val mockUrl = mockWebServer.url("/api/search")
        val sut = JiraRepository(mockUrl.toString())
        val result = runBlocking { sut.getTicketsForSprint("82.2", jiraUsername, jiraPassword) }
        assertEquals(2, result.size)
        assertEquals("TH-3668", result[0].key)
    }

    private fun ticketList() = TicketList(
        listOf(
            Issue("TH-3668", Fields("Here be dragons", "TH-1111", 0, Status("Done"))),
            Issue("TH-3669", Fields("Another one bites the dust", "TH-1112", 0, Status("Done")))
        )
    )
}