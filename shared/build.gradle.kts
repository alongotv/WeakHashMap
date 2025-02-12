import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.mavenPublish)
    id("signing")
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm()

    // region Apple
    listOf(
        macosArm64(),
        macosX64(),
        iosX64(),
        iosSimulatorArm64(),
        iosArm64(),
        watchosSimulatorArm64(),
        watchosX64(),
        watchosArm64(),
        watchosDeviceArm64(),
        tvosSimulatorArm64(),
        tvosX64(),
        tvosArm64(),
    ).forEach { appleTarget ->
        appleTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    // endregion

    // region Linux
    linuxX64()
    linuxArm64()
    // endregion

    // region Windows
    mingwX64()
    // endregion

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(
        "io.github.alongotv",
        "kotlin-multiplatform-weakhashmap",
        project.version.toString()
    )

    pom {
        name = "kotlin-multiplatform-weakhashmap"
        description =
            "This library brings WeakHashMap support for JVM and Native targets of Kotlin Multiplatform."
        url = "https://github.com/alongotv/WeakHashMap"
        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/license/mit"
            }
        }
        developers {
            developer {
                id = "alongotv"
                name = "Vladimir Vetrov"
            }
        }
        scm {
            url = "https://github.com/alongotv/WeakHashMap"
        }
    }
}
