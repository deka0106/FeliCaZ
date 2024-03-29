plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "work.deka.felicaz"
        minSdkVersion(22)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE")
    }
}

val kotlinVersion: String by project
val coroutinesVersion = "1.3.0-M1"

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("com.android.support:design:28.0.0")
    implementation("com.android.support:cardview-v7:28.0.0")
    implementation("com.android.support:recyclerview-v7:28.0.0")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("oauth.signpost:signpost-core:1.2.1.2")
    implementation("oauth.signpost:signpost-commonshttp4:1.2.1.2")
    implementation("com.univocity:univocity-parsers:2.8.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}

configurations.all {
    resolutionStrategy.eachDependency {
        when (requested.group) {
            "com.android.support" -> if (!requested.name.startsWith("multidex")) useVersion("28.+")
        }
    }
}
