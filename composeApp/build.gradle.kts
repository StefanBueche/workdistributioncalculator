import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization").version("2.0.0")
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        val desktopTest by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.core)
            implementation(libs.ktor.auth)
            implementation(libs.ktor.cio)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.json)
            implementation(libs.slf4j.simple)
            implementation(libs.androidx.lifecycle)
        }
        desktopTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockk)
        }
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.gls.parcelos"
            packageVersion = "1.0.0"
        }
    }
}
