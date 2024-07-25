import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.enums.EnumEntries


class EpicRepository {

    fun readSectionedProperties(filename: String): Map<EpicCategory, List<String>> {
        val sections = mutableMapOf<EpicCategory, List<String>>()
        val resourceStream = this::class.java.classLoader.getResourceAsStream(filename)
        val reader = resourceStream?.let { InputStreamReader(it) }?.let { BufferedReader(it) } ?:
                throw IllegalArgumentException("Resource not found: $filename")

        var currentSection = ArrayList<String>()

        reader.forEachLine { line ->
            when {
                line.isBlank() || line.startsWith("#") -> { }
                line.startsWith("[") && line.endsWith("]") -> {
                    val category = EpicCategory.getCategoryBySectionName(line.substring(1, line.length - 1))
                    currentSection = ArrayList()
                    sections[category] = currentSection
                }
                else -> {
                    currentSection.add(line)
                }
            }
        }

        return sections
    }
}

/*
 * The sectionNames for the enums must conform to the names of the sections in the file "epic-links.list".
 */
enum class EpicCategory(val sectionName: String) {
    MAINTENANCE("maintenance"),
    TECHNICAL_IMPROVEMENT("technical improvement"),
    BUSINESS("business");

    companion object {
        fun getCategoryBySectionName(sectionName: String): EpicCategory {
            return entries.first { it.sectionName == sectionName }
        }

        fun categories(): EnumEntries<EpicCategory> {
            return entries
        }
    }
}