package com.alongo

import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object GCUtils {
    @OptIn(NativeRuntimeApi::class)
    actual fun collect() {
        GC.collect()
    }
}