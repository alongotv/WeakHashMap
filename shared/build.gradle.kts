plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
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
        jvmMain {
            dependsOn(commonMain.get())
        }

        // region Apple main targets
        appleMain {
            dependsOn(commonMain.get())
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val macosX64Main by getting
        val macosArm64Main by getting

        iosMain {
            dependsOn(appleMain.get())
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            macosX64Main.dependsOn(this)
            macosArm64Main.dependsOn(this)

        }
        macosMain {
            dependsOn(appleMain.get())
        }
        // endregion

        commonTest {
            dependsOn(commonMain.get())
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        appleTest {
            dependsOn(appleMain.get())
            dependsOn(commonTest.get())
        }
        jvmTest {
            dependsOn(commonTest.get())
        }
    }
}
