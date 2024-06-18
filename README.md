# WeakHashMap KMP

WeakHashMap is a key-value data structure, where value gets deallocated after the key is
deallocated.
This data structure could come in handy, if you have short-lived objects to store in a Cache.

### Getting Started

At the moment the project is targeting the Native platforms, JVM.

1. Add Maven Central to the list of your repositories

``
repositories { mavenCentral() }
``

2. Add WeakHashMap as a dependency where it is required

``
   implementation("io.github.alongotv:kotlin-multiplatform-weakhashmap:$version") 
``

This is a substitute to be used until [ticket](https://youtrack.jetbrains.com/issue/KT-48075) for
adding WeakHashMap to Kotlin's stdlib is resolved.

The scheme below shows an approximate architecture of
the WeakHashMap.
![example](./img/weakhashmap_scheme.png)

When **Key Bearer Class** is collected by Garbage Collector, the **Key Object** and **Value Object** will get removed from WeakHashMap and deallocated too, provided
that **Key Object** has not been retained by some other object.