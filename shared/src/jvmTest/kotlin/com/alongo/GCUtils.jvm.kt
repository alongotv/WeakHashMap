package com.alongo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object GCUtils  {
    actual fun collect() {
        System.gc()
    }
}