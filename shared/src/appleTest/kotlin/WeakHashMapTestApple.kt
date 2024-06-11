import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.autoreleasepool
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WeakHashMapTestApple {
    private var weakHashMap = WeakHashMap<IntContainer, Any>()

    private val testIds = (100001..maxHashMapSize).map(::IntContainer)

    @OptIn(NativeRuntimeApi::class)
    @BeforeTest
    fun setup(): Unit = runBlocking {
        weakHashMap = WeakHashMap()
        kotlin.native.runtime.GC.collect()
        delay(100)
    }

    @OptIn(BetaInteropApi::class)
    @Test
    fun `test at least one autoreleased value is deallocated after map resizing`() = runBlocking {
        autoreleasepool {
            repeat(100000) {
                val intContainer = IntContainer(it)
                weakHashMap[intContainer] = Any()
            }
        }

        testIds.forEach {
            weakHashMap[it] = Any()
        }

        assertTrue { weakHashMap.containsKey(testIds.first()) }
        assertTrue { weakHashMap.containsKey(testIds.last()) }
        assertTrue { weakHashMap.size < maxHashMapSize }
    }

    @Test
    fun `test no values with strong keys deallocated`() = runBlocking {
        // Prepare
        testIds.forEach {
            weakHashMap[it] = Any()
        }

        assertTrue { weakHashMap.containsKey(testIds.first()) }
        assertTrue { weakHashMap.containsKey(testIds.last()) }
        assertEquals(expected = testIds.size, actual = weakHashMap.size)
    }

    @Test
    fun `test all values with strong keys deallocated after clear called`() = runBlocking {
        // Prepare
        testIds.forEach {
            weakHashMap[it] = Any()
        }

        // Do
        weakHashMap.clear()

        // Check
        assertFalse { weakHashMap.containsKey(testIds.first()) }
        assertFalse { weakHashMap.containsKey(testIds.last()) }
        assertEquals(expected = 0, actual = weakHashMap.size)
    }
}

private const val maxHashMapSize = 300000

data class IntContainer(val i: Int)