@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WeakHashMap<K, V>() {
    val entries: MutableSet<MutableEntry<K, V>>
    val keys: MutableSet<K>
    val size: Int
    val values: MutableCollection<V>

    fun isEmpty(): Boolean

    operator fun get(key: K): V?

    fun containsValue(value: V): Boolean
    fun containsKey(key: K): Boolean

    fun clear()
    operator fun set(key: K, value: V): V?
    fun putAll(from: Map<out K, V>)
    fun remove(key: K): V?
}

class MutableEntry<K, V>(private val map: WeakHashMap<K, V>, override val key: K, override val value: V) : MutableMap.MutableEntry<K, V> {
    override fun setValue(newValue: V): V {
        val oldValue = map[key]
        map[key] = value
        return oldValue!!
    }
}