@file:OptIn(ExperimentalNativeApi::class)

package com.alongo

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "unused")
actual class WeakHashMap<K : Any, V> {

    private val hashMap = hashMapOf<Key<K>, V>()

    private fun getMapInstance(): HashMap<Key<K>, V> {
        expungeStaleEntries()
        return hashMap
    }

    actual val size: Int
        get() {
            if (hashMap.size == 0) return 0
            expungeStaleEntries()
            return hashMap.size
        }

    actual val entries: MutableSet<MutableEntry<K, V>>
        get() {
            return keys.mapNotNull { key ->
                val value = get(key) ?: return@mapNotNull null
                MutableEntry(this, key, value)
            }.toMutableSet()
        }

    actual val keys: MutableSet<K>
        get() {
            return getMapInstance().keys.mapNotNull {
                it.getValue()
            }.toMutableSet()
        }

    actual val values: MutableCollection<V>
        get() = hashMap.values

    actual fun clear() {
        hashMap.clear()
    }

    actual fun isEmpty(): Boolean = hashMap.isEmpty()

    actual fun remove(key: K): V? {
        val k = Key(key)
        return hashMap.remove(k)
    }

    actual fun putAll(from: Map<out K, V>) {
        from.forEach { (key, value) ->
            set(key, value)
        }
    }

    actual operator fun set(key: K, value: V): V? {
        val map = getMapInstance()
        val k = Key(key)
        val prev = map[k]
        map[k] = value
        return prev
    }

    actual operator fun get(key: K): V? {
        val map = getMapInstance()
        val k = Key(key)
        return map[k]
    }

    actual fun containsValue(value: V): Boolean {
        return hashMap.containsValue(value)
    }

    actual fun containsKey(key: K): Boolean {
        val map = getMapInstance()
        val k = Key(key)
        return map.containsKey(k)
    }

    private fun expungeStaleEntries() {
        hashMap.keys
            .filterNot { it.isAvailable }
            .forEach {
                hashMap.remove(it)
            }
    }
}

private class Key<K : Any>(key: K) {

    private val ref = WeakReference(key)
    private val hash: Int = key.hashCode()

    @OptIn(ExperimentalNativeApi::class)
    val isAvailable get() = ref.value != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as Key<*>
        return ref.value == other.ref.value
    }

    fun getValue() = ref.value

    override fun hashCode(): Int = hash
}
