import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WorklogServiceTest {

    private lateinit var jiraRepository: JiraRepository
    private lateinit var epicRepository: EpicRepository
    private lateinit var sut: WorklogService

    @BeforeTest
    fun setup() {
        jiraRepository = mockk()
        epicRepository = mockk()
        sut = WorklogService(jiraRepository, epicRepository)
    }

    @Test
    fun `only maintenance tickets in Sprint sums only those`() {
        coEvery { jiraRepository.getTicketsForSprint(any(), any(), any()) } returns maintenanceTickets()
        every { epicRepository.readSectionedProperties(any()) } returns epicList()

        val result = sut.calculateTimeDistribution("", "", "")
        assertEquals(3, result[EpicCategory.MAINTENANCE])
        assertEquals(0, result[EpicCategory.TECHNICAL_IMPROVEMENT])
        assertEquals(0, result[EpicCategory.BUSINESS])
    }

    @Test
    fun `maintenance and technical tickets in Sprint sums only those`() {
        coEvery { jiraRepository.getTicketsForSprint(any(), any(), any()) } returns maintenanceAndTechnicalTickets()
        every { epicRepository.readSectionedProperties(any()) } returns epicList()

        val result = sut.calculateTimeDistribution("", "", "")
        assertEquals(3, result[EpicCategory.MAINTENANCE])
        assertEquals(4, result[EpicCategory.TECHNICAL_IMPROVEMENT])
        assertEquals(0, result[EpicCategory.BUSINESS])
    }

    @Test
    fun `mixed tickets in Sprint sums all`() {
        coEvery { jiraRepository.getTicketsForSprint(any(), any(), any()) } returns mixedTickets()
        every { epicRepository.readSectionedProperties(any()) } returns epicList()

        val result = sut.calculateTimeDistribution("", "", "")
        assertEquals(3, result[EpicCategory.MAINTENANCE])
        assertEquals(4, result[EpicCategory.TECHNICAL_IMPROVEMENT])
        assertEquals(6, result[EpicCategory.BUSINESS])
    }

    private fun epicList(): Map<EpicCategory, List<String>> {
        val epics = HashMap<EpicCategory, List<String>>()
        epics[EpicCategory.MAINTENANCE] = listOf("TH-1000", "TH-1001")
        epics[EpicCategory.TECHNICAL_IMPROVEMENT] = listOf("TH-2000", "TH-2001", "TH-2002")
        return epics
    }

    private fun maintenanceTickets(): List<Issue> {
        val ticketList = ArrayList<Issue>()
        ticketList.add(Issue("TH-1", Fields("1", "TH-1000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-2", Fields("2", "TH-1000", 7200, Status("Done"))))
        return ticketList
    }

    private fun maintenanceAndTechnicalTickets(): List<Issue> {
        val ticketList = ArrayList<Issue>()
        ticketList.add(Issue("TH-1", Fields("1", "TH-1000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-2", Fields("2", "TH-1001", 7200, Status("Done"))))
        ticketList.add(Issue("TH-10", Fields("10", "TH-2000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-11", Fields("11", "TH-2000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-12", Fields("12", "TH-2002", 7200, Status("Done"))))
        return ticketList
    }

    private fun mixedTickets(): List<Issue> {
        val ticketList = ArrayList<Issue>()
        ticketList.add(Issue("TH-20", Fields("20", "TH-3000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-21", Fields("21", "TH-3001", 3600, Status("Done"))))
        ticketList.add(Issue("TH-22", Fields("22", "TH-3002", 7200, Status("Done"))))
        ticketList.add(Issue("TH-1", Fields("1", "TH-1000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-2", Fields("2", "TH-1001", 7200, Status("Done"))))
        ticketList.add(Issue("TH-23", Fields("23", "TH-3002", 7200, Status("Done"))))
        ticketList.add(Issue("TH-10", Fields("10", "TH-2000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-11", Fields("11", "TH-2000", 3600, Status("Done"))))
        ticketList.add(Issue("TH-12", Fields("12", "TH-2002", 7200, Status("Done"))))
        return ticketList
    }

}