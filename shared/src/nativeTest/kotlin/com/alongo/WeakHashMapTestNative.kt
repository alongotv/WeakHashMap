package com.alongo

import kotlinx.coroutines.runBlocking
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WeakHashMapTestNative {
    private var weakHashMap = WeakHashMap<IntContainer, Any>()

    private val strongRefIds = (autoreleaseValuesCount.inc()..maxHashMapSize).map(::IntContainer)

    @OptIn(NativeRuntimeApi::class)
    @BeforeTest
    fun setup(): Unit = runBlocking {
        weakHashMap = WeakHashMap()
        GC.collect()
    }


    @OptIn(NativeRuntimeApi::class)
    @Test
    fun `all autoreleased values are deallocated and strong referred objs are retained`() =
        runBlocking {

            // Prepare
            fillMapWithAutoReleasedValues()
            fillMapWithStrongRefValues()

            GC.collect()

            // Check
            assertTrue {
                strongRefIds.all {
                    weakHashMap.containsKey(it)
                }
            }
            assertEquals(strongRefIds.size, weakHashMap.size)
        }

    @Test
    fun `test no values with strong keys are deallocated`() = runBlocking {

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
        assertFalse {
            strongRefIds.all {
                weakHashMap.containsKey(it)
            }
        }
        assertEquals(expected = 0, actual = weakHashMap.size)
    }

    @Test
    fun `test value with strong key deallocated after remove called`() = runBlocking {

        // Prepare
        fillMapWithStrongRefValues()

        val old = weakHashMap[strongRefIds.first()]
        assertNotNull(old)

        // Do
        weakHashMap.remove(strongRefIds.first())

        // Check
        assertFalse { weakHashMap.containsKey(strongRefIds.first()) }
        assertFalse { weakHashMap.containsValue(old) }
        assertEquals(expected = strongRefIds.size.dec(), actual = weakHashMap.size)
    }

    @OptIn(NativeRuntimeApi::class)
    @Test
    fun `map contains no keys after all key objects are deallocated`() = runBlocking {

        // Prepare
        fillMapWithAutoReleasedValues()

        // Do
        GC.collect()

        // Check
        assertEquals(emptySize, weakHashMap.keys.size)
    }

    private fun fillMapWithStrongRefValues() {
        strongRefIds.forEach {
            weakHashMap[it] = Any()
        }
    }

    /**
     * Regarding Kotlin/Native GC:
     * In order for object to be garbage collected, parent function where the object has been created
     * has to complete its execution. (even if this object has been replaced with a null)
     * Mind that's also true for using the WeakHashMap in production code.
     * Therefore, we have to move logic that fills map with values to a separate function.
     **/
    private fun fillMapWithAutoReleasedValues() {
        repeat(autoreleaseValuesCount) {
            val intContainer = IntContainer(it)
            weakHashMap[intContainer] = Any()
        }
    }
}

private const val emptySize = 0
private const val autoreleaseValuesCount = 1000

// The max is here for testing purposes, in real tasks hashmap could exceed this size
private const val maxHashMapSize = 3000