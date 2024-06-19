package com.alongo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object GCUtils  {
    actual fun collect() {
        System.gc()
        // "When control returns from the method call, the Java Virtual Machine
        // has made a best effort to reclaim space from all discarded objects."
        // This could mean the GC has completed its job, as well as it could not.
        // Therefore, we have to wait a bit to make a chance of GC completion higher.
        Thread.sleep(3000L)
    }
}