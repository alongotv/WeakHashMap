import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.autoreleasepool
import kotlinx.coroutines.runBlocking
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WeakHashMapTestApple {
    private var weakHashMap = WeakHashMap<IntContainer, Any>()

    private val strongRefIds = (100001..maxHashMapSize).map(::IntContainer)

    @OptIn(NativeRuntimeApi::class)
    @BeforeTest
    fun setup(): Unit = runBlocking {
        weakHashMap = WeakHashMap()
        kotlin.native.runtime.GC.collect()
    }

    @Test
    fun `test at least one autoreleased value is deallocated after map resizing`() = runBlocking {

        // Prepare
        fillMapWithAutoReleasedValues()
        fillMapWithStrongRefValues()

        // Check
        assertTrue { weakHashMap.containsKey(strongRefIds.first()) }
        assertTrue { weakHashMap.containsKey(strongRefIds.last()) }
        assertTrue { weakHashMap.size < maxHashMapSize }
    }

    @Test
    fun `test no values with strong keys deallocated`() = runBlocking {

        // Prepare
        fillMapWithStrongRefValues()

        // Check
        assertTrue { weakHashMap.containsKey(strongRefIds.first()) }
        assertTrue { weakHashMap.containsKey(strongRefIds.last()) }
        assertEquals(expected = strongRefIds.size, actual = weakHashMap.size)
    }

    @Test
    fun `test all values with strong keys deallocated after clear called`() = runBlocking {

        // Prepare
        fillMapWithStrongRefValues()

        // Do
        weakHashMap.clear()

        // Check
        assertFalse { weakHashMap.containsKey(strongRefIds.first()) }
        assertFalse { weakHashMap.containsKey(strongRefIds.last()) }
        assertEquals(expected = 0, actual = weakHashMap.size)
    }

    @Test
    fun `test value with strong key deallocated after remove called`() = runBlocking {

        // Prepare
        fillMapWithStrongRefValues()

        val old = weakHashMap[strongRefIds.first()]
        assertNotNull(old)

        weakHashMap.remove(strongRefIds.first())

        // Check
        assertFalse { weakHashMap.containsKey(strongRefIds.first()) }
        assertFalse { weakHashMap.containsValue(old) }
        assertEquals(expected = strongRefIds.size - 1, actual = weakHashMap.size)
    }


    private fun fillMapWithStrongRefValues() {
        strongRefIds.forEach {
            weakHashMap[it] = Any()
        }
    }

    /**
     * Note: autoreleasepool is used for testing purposes here,
     * in production code obsolete object collection should be handled by Kotlin/Native GC
     **/
    @OptIn(BetaInteropApi::class)
    private fun fillMapWithAutoReleasedValues() {
        autoreleasepool {
            repeat(100000) {
                val intContainer = IntContainer(it)
                weakHashMap[intContainer] = Any()
            }
        }
    }
}

private const val maxHashMapSize = 300000

data class IntContainer(val i: Int)