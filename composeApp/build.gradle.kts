import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.konan.target.Family

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)

    //kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
                cssSupport {
                    enabled.set(true)
                }
                /*testTask {
                    useKarma {
                        useIe()
                        useSafari()
                        useFirefox()
                        useChrome()
                        useChromeCanary()
                        useChromeHeadless()
                        usePhantomJS()
                        useOpera()
                    }
                }*/
            }
        }
        binaries.executable()
    }

    targets
        .filterIsInstance<KotlinNativeTarget>()
        .filter { it.konanTarget.family == Family.IOS }
        .forEach {
            it.binaries.framework {

                //export("com.arkivanov.decompose:decompose:<version>")
                //export("com.arkivanov.essenty:lifecycle:<essenty_version>")

                // Optional, only if you need state preservation on Darwin (Apple) targets
                //export("com.arkivanov.essenty:state-keeper:<essenty_version>")

                // Optional, only if you need state preservation on Darwin (Apple) targets
                //export("com.arkivanov.parcelize.darwin:runtime:<parcelize_darwin_version>")
            }
        }
    
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
        val wasmJsMain by getting
        
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.android)
            implementation(libs.kotlinx.coroutines.android)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.javafx)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            implementation(libs.ktor.client.apache5)
            implementation(libs.kotlinx.coroutines.swing)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.ktor.client.js.wasm)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            implementation(libs.navigation.compose)

            implementation(libs.decompose.extension)
            implementation(libs.decompose)
            implementation(libs.material3.window.size)
            implementation(libs.compottie)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.serialization)
            implementation(libs.bundles.ktor.common)

            implementation(libs.multiplatform.settings)

            implementation(libs.kodein.di)

            implementation(libs.coil.compose)
            implementation(libs.coil.network)

            implementation(libs.lifecycle.runtime)
            implementation(libs.lifecycle.viewmodel)

            // doesn't support wasmJS (yet) implementation("androidx.paging:paging-common:3.3.0-rc01")
        }
    }
}

configurations.all {
    /*this.exclude(
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-core"
    )*/
    /*this.exclude(
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-jdk8"
    )
    this.exclude(
        "org.jetbrains.kotlinx",
        "kotlinx-coroutines-core-jvm"
    )*/
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.WARNING // NONE | FAIL
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false // true
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = false // true
}

android {
    namespace = "cz.tipsport.rododendron"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "cz.tipsport.rododendron"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        //javaHome = System.getenv("JDK_17")
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Tipsys3 demo"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}