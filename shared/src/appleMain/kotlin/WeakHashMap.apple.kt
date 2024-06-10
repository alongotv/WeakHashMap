@file:Suppress("UNCHECKED_CAST")

import platform.Foundation.NSMapTable
import platform.Foundation.allObjects

// 32-bit weak hashmap
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "unused")
actual class WeakHashMap<K, V> {

    /** When the key is released, retained value should be released shortly
     * after the MapTable is resized
     * @see <a href="https://developer.apple.com/library/archive/releasenotes/Foundation/RN-FoundationOlderNotes/#//apple_ref/doc/uid/TP40008080-TRANSLATED_CHAPTER_965-TRANSLATED_DEST_999B">Mountain Lion Documentation</a> explaining this behavior
     * (NSMapTable zeroing weak changes)
     **/
    private val mapTable: NSMapTable = NSMapTable.weakToStrongObjectsMapTable()

    actual val size: Int
        get() = mapTable.count().toInt()

    actual val entries: MutableSet<MutableEntry<K, V>>
        get() {
            return keys.map { key ->
                MutableEntry(this, key, mapTable.objectForKey(key) as V)
            }.toMutableSet()
        }

    actual val keys: MutableSet<K>
        get() {
            return mapTable.keyEnumerator().allObjects().map {
                it as K
            }.toMutableSet()
        }
    actual val values: MutableCollection<V>
        get() {
            return mapTable.objectEnumerator()?.allObjects()?.map {
                it as V
            }.orEmpty().toMutableSet()
        }

    actual fun clear() {
        mapTable.removeAllObjects()
    }

    actual fun isEmpty(): Boolean {
        return mapTable.count == 0uL
    }

    actual fun remove(key: K): V? {
        val oldValue = mapTable.objectForKey(key) as V?
        mapTable.removeObjectForKey(aKey = key)
        return oldValue
    }

    actual fun putAll(from: Map<out K, V>) {
        from.forEach { (key, value) ->
            set(key, value)
        }
    }

    actual operator fun set(key: K, value: V): V? {
        val prev = mapTable.objectForKey(key) as V?
        mapTable.setObject(anObject = value, forKey = key)
        return prev
    }

    actual operator fun get(key: K): V? {
        return mapTable.objectForKey(aKey = key) as V?
    }

    actual fun containsValue(value: V): Boolean {
        return mapTable.objectEnumerator()?.allObjects()?.contains(value) == true
    }

    actual fun containsKey(key: K): Boolean {
        return mapTable.keyEnumerator().allObjects().contains(key)
    }
}
