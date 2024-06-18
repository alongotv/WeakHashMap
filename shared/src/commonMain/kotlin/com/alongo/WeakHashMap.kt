package com.alongo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WeakHashMap<K : Any, V>() {
    /**
     * Retuns a [MutableSet] of [MutableEntry] stored in the data structure.
     **/
    val entries: MutableSet<MutableEntry<K, V>>

    /**
     * Retuns a [MutableSet] of Keys stored in the data structure.
     **/
    val keys: MutableSet<K>

    /**
     * Returns a number of entries stored in the data structure.
     **/
    val size: Int

    /**
     * Retuns a [MutableCollection] of values stored in the data structure.
     **/
    val values: MutableCollection<V>

    /**
     * Returns a boolean indicating if the data structure contains no entries.
     **/
    fun isEmpty(): Boolean

    /**
     * Given a [key], returns an object or null – if the object for the specified key does not
     * exist or is null.
     **/
    operator fun get(key: K): V?

    /**
     * Given a [value], returns a boolean indicating if this [value] is stored in the data structure.
     **/
    fun containsValue(value: V): Boolean

    /**
     * Given a [key], returns a boolean indicating if this [key] is stored in the data structure.
     **/
    fun containsKey(key: K): Boolean

    /**
     * Removes all elements from the data structure.
     **/
    fun clear()

    /**
     * Given a [key] with associated [value], stores them in the data structure.
     *
     * Returns an object or null – if the value for the specified key did not
     * exist before storing new [key] and [value], or was null.
     **/
    operator fun set(key: K, value: V): V?

    /**
     * Given a [from] map, stores its' contents in the data structure.
     **/
    fun putAll(from: Map<out K, V>)

    /**
     * Given a [key], removes an entry associated with this [key] from the data structure.
     *
     * Returns an object or null – if the value for the specified key did not
     * exist before removing the entry, or was null.
     **/
    fun remove(key: K): V?
}

/**
 * Helper class to unify the format of data provided by the [WeakHashMap]
 **/
class MutableEntry<K : Any, V>(
    private val map: WeakHashMap<K, V>,
    override val key: K,
    override val value: V
) : MutableMap.MutableEntry<K, V> {
    override fun setValue(newValue: V): V {
        val oldValue = map[key]
        map[key] = value
        return oldValue!!
    }
}