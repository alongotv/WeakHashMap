package com.alongo

import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import kotlin.test.Test
import kotlin.test.assertEquals

class WeakHashMapTest {

    private val weakHashMap = WeakHashMap<IntContainer, Any>()

    @Test
    fun `map is empty after the only key is deallocated`() = runBlocking {

        val weak = WeakReference(IntContainer(0))

        weak.get()?.let {
            weakHashMap[it] = Any()
        }
        System.gc()
        Thread.sleep(1000)

        assertEquals(0, weakHashMap.keys.size)
    }
}