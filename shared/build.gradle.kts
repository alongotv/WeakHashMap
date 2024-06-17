plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("maven-publish")
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

val nexusUsername = providers.gradleProperty("nexusUsername")
val nexusPassword = providers.gradleProperty("nexusPassword")
val emptyJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        publications.withType<MavenPublication>().configureEach {
            val publication = this
            val javadocJar = tasks.register("${publication.name}JavadocJar", Jar::class) {
                archiveClassifier.set("javadoc")
                archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")
            }
            artifact(javadocJar)

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
    }
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://central.sonatype.com/api/v1/publisher/upload")
            credentials {
                username = nexusUsername.get()
                password = nexusPassword.get()
            }
        }
    }
}
