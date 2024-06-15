package com.alongo

import java.util.WeakHashMap

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "unused")
actual class WeakHashMap<K: Any, V> {
    private val weakHashMap = WeakHashMap<K, V>()

    actual val entries: MutableSet<MutableEntry<K, V>>
        get() = weakHashMap.entries.map {
            MutableEntry(this, it.key, it.value)
        }.toMutableSet()

    actual val keys: MutableSet<K>
        get() = weakHashMap.keys
    actual val size: Int
        get() = weakHashMap.size
    actual val values: MutableCollection<V>
        get() = weakHashMap.values

    actual fun isEmpty(): Boolean {
        return weakHashMap.isEmpty()
    }

    actual operator fun get(key: K): V? {
        return weakHashMap[key]
    }

    actual fun containsValue(value: V): Boolean {
        return weakHashMap.containsValue(value)
    }

    actual fun containsKey(key: K): Boolean {
        return weakHashMap.containsKey(key)
    }

    actual fun clear() {
        weakHashMap.clear()
    }

    actual fun remove(key: K): V? {
        return weakHashMap.remove(key)
    }

    actual fun putAll(from: Map<out K, V>) {
        weakHashMap.putAll(from)
    }

    actual operator fun set(key: K, value: V): V? {
        return weakHashMap.put(key, value)
    }
}
