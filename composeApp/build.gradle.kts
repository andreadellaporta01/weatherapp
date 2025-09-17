import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
}

val secretProperties = Properties()
val secretPropertiesFile = rootProject.file("secret.properties")
if (secretPropertiesFile.exists()) {
    secretProperties.load(secretPropertiesFile.inputStream())
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        dependencies {
            androidTestImplementation(libs.androidx.ui.test.junit4.android)
            debugImplementation(libs.androidx.ui.test.manifest)
        }
    }
    
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
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                        add("$projectDirPath/src/commonMain/composeResources")
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        val desktopTest by getting
        
        androidMain.dependencies {
            // Android-specific Ktor client
            implementation(libs.ktor.client.okhttp)

            // Android Coroutines
            implementation(libs.kotlinx.coroutines.android)

            // Koin Android
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            // DataStore
            implementation(libs.androidx.datastore.datastore)
            implementation(libs.androidx.datastore.preferences)

            // Geocoding and Permissions
            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.permissions.mobile)
        }
        commonMain.dependencies {
            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Ktor networking
            implementation(libs.bundles.ktor)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Dependency Injection
            implementation(libs.koin.core)
            implementation(libs.koin.viewmodel)

            // Date/Time
            implementation(libs.kotlinx.datetime)

            // ViewModel
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)

            // Navigation
            implementation(libs.androidx.navigation.compose)

            // Icons
            implementation(libs.compose.material.icons)

            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.bottomSheetNavigator)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.transitions)

            // Permissions and Geolocation
            implementation(libs.compass.geolocation)

            // State Holder
            implementation(libs.stateHolder)
            implementation(libs.stateHolder.voyager)
            implementation(libs.stateHolder.compose)

            // Image loading
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
        }
        wasmJsMain.dependencies {
            implementation(libs.compass.geolocation.browser)
        }
        iosMain.dependencies {
            // iOS-specific Ktor client
            implementation(libs.ktor.client.darwin)
            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.permissions.mobile)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.junit)
            implementation(libs.koin.test)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio)
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.dellapp.weatherapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dellapp.weatherapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    signingConfigs {
        create("release") {
            storeFile = rootProject.file("weatherapp-release-upload-keystore.jks")
            storePassword = getSecret("ANDROID_PASSWORD")
            keyAlias = getSecret("ANDROID_ALIAS")
            keyPassword = getSecret("ANDROID_PASSWORD")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.dellapp.weatherapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.dellapp.weatherapp"
            packageVersion = "1.0.0"

            macOS {
                iconFile.set(project.file("src/desktopMain/resources/icon.icns"))
                bundleID = "com.dellapp.weatherapp"
                signing {
                    sign.set(false)
                }
            }

            windows {
                iconFile.set(project.file("src/desktopMain/resources/icon.ico"))
            }

            linux {
                iconFile.set(project.file("src/desktopMain/resources/icon.png"))
            }
        }
    }
}

abstract class GenerateDrawableMap : DefaultTask() {

    @get:InputDirectory
    abstract val drawableDir: DirectoryProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val packageName = "weatherapp.composeapp.generated.resources"
        val generatedPackageName = "weatherapp.composeapp.generated"

        val files = drawableDir.get().asFile.listFiles { f ->
            f.isFile && f.name.endsWith(".svg")
        } ?: emptyArray()

        val imports = files.joinToString("\n") { file ->
            val name = file.nameWithoutExtension
            "import $packageName.$name"
        }

        val entries = files.joinToString(",\n") { file ->
            val name = file.nameWithoutExtension
            "    \"$name\" to Res.drawable.$name"
        }

        val stringVals = files.joinToString("\n") { file ->
            val name = file.nameWithoutExtension
            val camelName = toCamelCase(name)
            "val $camelName = \"$name\""
        }

        val content = """
            |// AUTO-GENERATED FILE. DO NOT EDIT.
            |package $generatedPackageName
            |
            |import org.jetbrains.compose.resources.DrawableResource
            |import $packageName.Res
            |$imports
            |
            |$stringVals
            |
            |val drawableMap: Map<String, DrawableResource> = mapOf(
            |$entries
            |)
        """.trimMargin()

        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(content)
    }

    private fun toCamelCase(input: String): String {
        return input.split("_", "-", " ")
            .filter { it.isNotBlank() }
            .joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
            .replaceFirstChar(Char::lowercaseChar)
    }
}

val generateDrawableMap by tasks.registering(GenerateDrawableMap::class) {
    drawableDir.set(layout.projectDirectory.dir("src/commonMain/composeResources/drawable"))
    outputFile.set(layout.buildDirectory.file("generated/drawablemap/GeneratedDrawableMap.kt"))
}

kotlin.sourceSets.named("commonMain") {
    kotlin.srcDir(generateDrawableMap.map { it.outputFile.get().asFile.parentFile })
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(generateDrawableMap)
}

abstract class GenerateApiConfigTask : DefaultTask() {
    @get:Input
    abstract val apiKey: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        outputFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText("""
                package config
                
                internal object ApiConfig {
                    const val API_KEY = "${apiKey.get()}"
                }
            """.trimIndent())
        }
    }
}

val generateApiConfig = tasks.register<GenerateApiConfigTask>("generateApiConfig") {
    apiKey.set(secretProperties.getProperty("API_KEY", ""))
    outputFile.set(layout.buildDirectory.file("generated/kotlin/config/ApiConfig.kt"))
}

val copyApiConfig = tasks.register<Copy>("copyApiConfig") {
    dependsOn(generateApiConfig)
    from(generateApiConfig.flatMap { it.outputFile })
    into("src/commonMain/kotlin/config/")
}

abstract class UpdatePlistVersion : DefaultTask() {

    @get:InputFile
    abstract val plistFile: RegularFileProperty

    @get:Input
    abstract var appVersion: String

    @get:Input
    abstract var buildVersion: String

    @TaskAction
    fun update() {
        val file = plistFile.get().asFile

        if (!file.exists()) {
            throw GradleException("Info.plist not found at ${file.absolutePath}")
        }

        var plistContent = file.readText()

        plistContent = plistContent.replace(
            Regex("<key>CFBundleShortVersionString</key>\\s*<string>.*?</string>"),
            "<key>CFBundleShortVersionString</key>\n    <string>$appVersion</string>"
        )
        plistContent = plistContent.replace(
            Regex("<key>CFBundleVersion</key>\\s*<string>.*?</string>"),
            "<key>CFBundleVersion</key>\n    <string>$buildVersion</string>"
        )

        file.writeText(plistContent)
    }
}

val updatePlistVersion = tasks.register<UpdatePlistVersion>("updatePlistVersion") {
    plistFile.set(layout.projectDirectory.file("../iosApp/iosApp/Info.plist"))
    appVersion = libs.versions.versionName.get()
    buildVersion = libs.versions.versionCode.get()
}

tasks.named("generateComposeResClass") {
    dependsOn(copyApiConfig)
    dependsOn(updatePlistVersion)
}

fun getSecret(propertyName: String): String {
    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        val properties = Properties()
        secretsFile.inputStream().use { properties.load(it) }
        val property = properties.getProperty(propertyName)
        return property
    } else return "invalid"
}


