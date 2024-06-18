import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.vanniktech.maven.publish") version "0.28.0"
    id("signing")
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64()
    ).forEach { appleTarget ->
        appleTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

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

val nexusUsername = providers.gradleProperty("mavenCentralUsername")
val nexusPassword = providers.gradleProperty("mavenCentralPassword")

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("io.github.alongotv", "kotlin-multiplatform-weakhashmap", "0.0.1")

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
