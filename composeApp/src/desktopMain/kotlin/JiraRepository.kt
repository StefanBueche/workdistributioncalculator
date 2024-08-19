import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class JiraRepository(private val jiraUrl: String) {

    suspend fun getTicketsForSprint(sprint: String, jiraUsername: String, jiraPassword: String): List<Issue> {
        val query = "project = TH AND issueType in (Story, Task, Bug) AND Sprint = $sprint AND status = Done"
        val jiraTickets: TicketList

        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(username = jiraUsername, password = jiraPassword)
                    }
                    sendWithoutRequest { true }
                }
            }
        }

        jiraTickets = client.get(jiraUrl) {
            url {
                parameters.append("jql", query)
                parameters.append("startAt", "0")
                parameters.append("maxResults", "1000")
            }
        }.body()

        client.close()
        return jiraTickets.issues
    }
}