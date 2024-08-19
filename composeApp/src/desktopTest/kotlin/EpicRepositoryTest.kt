import org.junit.Assert.assertEquals
import kotlin.test.Test

class EpicRepositoryTest {

    @Test
    fun `file with only maintenance section creates only this EpicCategory`() {
        val epicList = ArrayList<String>()
        epicList.add("TH-1111")
        epicList.add("TH-2222")
        val expected = HashMap<EpicCategory, List<String>>()
        expected[EpicCategory.MAINTENANCE] = epicList

        val filename = "epics-only-maintenance.list"
        val sut = EpicRepository()
        val result = sut.readSectionedProperties(filename)

        assertEquals(expected, result)
    }

    @Test
    fun `file with maintenance and improvement sections returns both EpicCategories`() {
        var epicList = ArrayList<String>()
        epicList.add("TH-1111")
        epicList.add("TH-2222")
        val expected = HashMap<EpicCategory, List<String>>()
        expected[EpicCategory.MAINTENANCE] = epicList
        epicList = ArrayList()
        epicList.add("TH-9997")
        epicList.add("TH-9998")
        epicList.add("TH-9999")
        expected[EpicCategory.TECHNICAL_IMPROVEMENT] = epicList

        val filename = "epics-maintenance-improvement.list"
        val sut = EpicRepository()
        val result = sut.readSectionedProperties(filename)

        assertEquals(expected, result)
    }
}
