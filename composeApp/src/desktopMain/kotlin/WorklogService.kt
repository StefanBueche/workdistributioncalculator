import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class WorklogService(private val jiraRepository: JiraRepository, private val epicRepository: EpicRepository) {

    val logger = LoggerFactory.getLogger(WorklogService::class.java)

    fun calculateTimeDistribution(sprintName: String, jiraUsername: String, jiraPassword: String): Map<EpicCategory, Int> {
        val ticketsInSprint = runBlocking {
            jiraRepository.getTicketsForSprint(sprintName, jiraUsername, jiraPassword)
        }
        val epicsByCategory = epicRepository.readSectionedProperties("epic-links.list")

        val timeDistribution = HashMap<EpicCategory, Int>()
        for (epicCategory in EpicCategory.categories()) {
            timeDistribution[epicCategory] = sumCategoryTickets(ticketsInSprint, epicsByCategory, epicCategory)
        }
        timeDistribution[EpicCategory.BUSINESS] = sumBusinessTickets(ticketsInSprint, epicsByCategory)
        logger.debug("Business: {}", timeDistribution[EpicCategory.BUSINESS])
        logger.debug("Maintenance: {}", timeDistribution[EpicCategory.MAINTENANCE])
        logger.debug("Technical Improvement: {}", timeDistribution[EpicCategory.TECHNICAL_IMPROVEMENT])
        timeDistribution[EpicCategory.HEY_JOES] = sumHeyJoeTickets(ticketsInSprint)
        logger.debug("Hey Joes: {}", timeDistribution[EpicCategory.HEY_JOES])
        timeDistribution[EpicCategory.MAINTENANCE] =
            timeDistribution[EpicCategory.MAINTENANCE]!!.minus(timeDistribution[EpicCategory.HEY_JOES]!!)
        logger.debug("Maintenance after deducting Hey Joes: {}", timeDistribution[EpicCategory.MAINTENANCE])
        return timeDistribution
    }

    private fun sumHeyJoeTickets(ticketsInSprint: List<Issue>) =
        ticketsInSprint.filter { it.fields.summary == "Hey Joes" }.sumOf { it.fields.aggregatetimespent ?: 0 }.inHours()

    private fun sumCategoryTickets(
        ticketsInSprint: List<Issue>,
        epicsByCategory: Map<EpicCategory, List<String>>,
        epicCategory: EpicCategory
    ): Int {
        val epics = epicsByCategory[epicCategory]
        val ticketsInCategory = ticketsInSprint.filter { epics?.contains(it.fields.epicLink) ?: false }
        return ticketsInCategory.sumOf { it.fields.aggregatetimespent ?: 0 }.inHours()
    }

    private fun sumBusinessTickets(
        ticketsInSprint: List<Issue>,
        epicsByCategory: Map<EpicCategory, List<String>>
    ) = ticketsInSprint
        .filter { !(epicsByCategory[EpicCategory.MAINTENANCE]?.contains(it.fields.epicLink) ?: false) }
        .filter { !(epicsByCategory[EpicCategory.TECHNICAL_IMPROVEMENT]?.contains(it.fields.epicLink) ?: false) }
        .sumOf { it.fields.aggregatetimespent ?: 0 }.inHours()

}

private fun Int.inHours(): Int {
    return this / 3600
}