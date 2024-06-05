import platform.Foundation.NSMapTable

// 32-bit weak hashmap
actual class WeakHashMap<K, V> : MutableMap<K, V> {
    private val mapTable: NSMapTable = NSMapTable()

    actual override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = TODO("Not yet implemented")
    actual override val keys: MutableSet<K>
        get() = TODO("Not yet implemented")

    actual override val size: Int
        get() = mapTable.count().toInt()
    actual override val values: MutableCollection<V>
        get() = TODO("Not yet implemented")

    override fun clear() {
        TODO("Not yet implemented")
    }

    actual override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun remove(key: K): V? {
        TODO("Not yet implemented")
    }

    override fun putAll(from: Map<out K, V>) {
        TODO("Not yet implemented")
    }

    override fun put(key: K, value: V): V? {
        TODO("Not yet implemented")
    }

    actual override fun get(key: K): V? {
        TODO("Not yet implemented")
    }

    actual override fun containsValue(value: V): Boolean {
        TODO("Not yet implemented")
    }

    actual override fun containsKey(key: K): Boolean {
        TODO("Not yet implemented")
    }

}