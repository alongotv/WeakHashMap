import java.util.WeakHashMap

actual class WeakHashMap<K, V> : MutableMap<K, V> {
    private val weakHashMap = WeakHashMap<K, V>()

    actual override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = weakHashMap.entries
    actual override val keys
        get() = weakHashMap.keys
    actual override val size: Int
        get() = weakHashMap.size
    actual override val values: MutableCollection<V>
        get() = weakHashMap.values

    actual override fun isEmpty(): Boolean {
        return weakHashMap.isEmpty()
    }

    actual override fun get(key: K): V? {
        return weakHashMap[key]
    }

    actual override fun containsValue(value: V): Boolean {
        return weakHashMap.containsValue(value)
    }

    actual override fun containsKey(key: K): Boolean {
        return weakHashMap.containsKey(key)
    }

    override fun clear() {
        weakHashMap.clear()
    }

    override fun remove(key: K): V? {
        return weakHashMap.remove(key)
    }

    override fun putAll(from: Map<out K, V>) {
        weakHashMap.putAll(from)
    }

    override fun put(key: K, value: V): V? {
        return weakHashMap.put(key, value)
    }
}
