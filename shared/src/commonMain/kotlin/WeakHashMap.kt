expect class WeakHashMap<K, V> : MutableMap<K, V> {
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
    override val keys: MutableSet<K>
    override val size: Int
    override val values: MutableCollection<V>

    override fun isEmpty(): Boolean

    override fun get(key: K): V?

    override fun containsValue(value: V): Boolean
    override fun containsKey(key: K): Boolean
}