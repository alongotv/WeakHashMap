import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.autoreleasepool
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
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

        testIds.forEach {
            weakHashMap[it] = Any()
        }
        println(weakHashMap.size)

        assertTrue { weakHashMap.containsKey(testIds.first()) }
        assertTrue { weakHashMap.containsKey(testIds.last()) }
        assertEquals(expected = testIds.size, actual = weakHashMap.size)
    }
}

private const val maxHashMapSize = 300000

data class IntContainer(val i: Int)