import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "dualscore"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DualScore"
            packageVersion = "1.0.0"
            nativeDistributions {
                macOS {
                    iconFile.set(project.file("icon/mac_icon.icns"))
                }
                windows {
                    iconFile.set(project.file("icon/windows_icon.ico"))
                }
                linux {
                    iconFile.set(project.file("icon/linux_icon.png"))
                }
            }
        }
    }
}
