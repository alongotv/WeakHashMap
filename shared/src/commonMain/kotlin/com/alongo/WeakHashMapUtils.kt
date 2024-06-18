package com.alongo

/**
 * Returns an empty new [WeakHashMap].
 **/
@Suppress("unused")
fun <K : Any, V> weakHashMapOf(): WeakHashMap<K, V> = WeakHashMap()

/**
 * Returns a new [WeakHashMap] with the specified contents, given as a list of pairs where the first component is the key and the second is the value.
 **/
@Suppress("unused")
fun <K : Any, V> weakHashMapOf(vararg pairs: Pair<K, V>): WeakHashMap<K, V> =
    WeakHashMap<K, V>().apply { putAll(pairs.toMap()) }
