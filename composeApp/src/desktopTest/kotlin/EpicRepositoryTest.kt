import org.junit.Assert.assertEquals
import kotlin.test.Test

class EpicRepositoryTest {

    @Test
    fun `file with only maintenance section creates only this EpicCategory`() {
        val expected = HashMap<EpicCategory, List<String>>()
        expected[EpicCategory.MAINTENANCE] = listOf("TH-1111", "TH-2222")

        val filename = "epics-only-maintenance.list"
        val sut = EpicRepository()
        val result = sut.readSectionedProperties(filename)

        assertEquals(expected, result)
    }

    @Test
    fun `file with maintenance and improvement sections returns both EpicCategories`() {
        val expected = HashMap<EpicCategory, List<String>>()
        expected[EpicCategory.MAINTENANCE] = listOf("TH-1111", "TH-2222")
        expected[EpicCategory.TECHNICAL_IMPROVEMENT] = listOf("TH-9997", "TH-9998", "TH-9999")

        val filename = "epics-maintenance-improvement.list"
        val sut = EpicRepository()
        val result = sut.readSectionedProperties(filename)

        assertEquals(expected, result)
    }
}
