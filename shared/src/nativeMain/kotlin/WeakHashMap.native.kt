@file:OptIn(ExperimentalNativeApi::class)

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "unused")
actual class WeakHashMap<K : Any, V> {

    private val hashMap = hashMapOf<Key<K>, V>()

    actual val size: Int
        get() = hashMap.count()

    actual val entries: MutableSet<MutableEntry<K, V>>
        get() {
            return keys.mapNotNull { key ->
                val value = get(key) ?: return@mapNotNull null
                MutableEntry(this, key, value)
            }.toMutableSet()
        }

    actual val keys: MutableSet<K>
        get() {
            return hashMap.keys.mapNotNull {
                it.getValue()
            }.toMutableSet()
        }

    actual val values: MutableCollection<V>
        get() {
            return hashMap.values
        }

    actual fun clear() {
        hashMap.clear()
    }

    actual fun isEmpty(): Boolean {
        return hashMap.isEmpty()
    }

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
        val k = Key(key)
        val prev = hashMap[k]
        hashMap[k] = value
        return prev
    }

    actual operator fun get(key: K): V? {
        val k = Key(key)
        return hashMap[k]
    }

    actual fun containsValue(value: V): Boolean {
        return hashMap.values.any {
            it == value
        }
    }

    actual fun containsKey(key: K): Boolean {
        clean()
        val k = Key(key)
        return hashMap.keys.any {
            it == k
        }
    }

    private fun clean() {
        hashMap.keys
            .filter { !it.isAvailable }
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

    internal fun getValue() = ref.value

    override fun hashCode(): Int = hash
}
