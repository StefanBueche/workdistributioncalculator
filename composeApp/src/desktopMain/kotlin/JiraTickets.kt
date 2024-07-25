import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketList(val issues: List<Issue>)

@Serializable
data class Issue(val key: String, val fields: Fields)

@Serializable
data class Fields(
    val summary: String,
    @SerialName("customfield_10002") val epicLink: String,
    val aggregatetimespent: Int?,
    val status: Status
)

@Serializable
data class Status(val name: String)
