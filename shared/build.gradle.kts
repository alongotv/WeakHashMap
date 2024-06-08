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
        appleMain {
            dependsOn(commonMain.get())
        }
        jvmMain {
            dependsOn(commonMain.get())
        }
        commonTest {
            dependencies {
                kotlin("test-common")
                kotlin("test-annotations-common")
            }
        }
        appleTest {
            dependsOn(commonTest.get())
        }
        jvmTest {
            dependsOn(commonTest.get())
        }
    }
}
