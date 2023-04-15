import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "space.arlet"
version = "1.3.0-beta"

val artifactId = "VKSendBot"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

plugins {
    id("java")
    kotlin("jvm") version "1.8.20"
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.compose") version "1.4.0"
}

val mainClassName = "Application"
val kotlinVersion = "1.8.20"
val javaVersion = "11"

dependencies {
    implementation("com.vk.api:sdk:1.0.14")
    testImplementation("junit:junit:4.13.2")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    implementation(compose.desktop.windows_x64)
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.macos_x64)
}

application {
    mainClass.set(mainClassName)
}

tasks.named<JavaCompile>("compileJava") {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    options.encoding = "UTF-8"
}

tasks.named<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(artifactId)
    archiveClassifier.set("")
    archiveVersion.set(version.toString())

    destinationDirectory.set(file("."))

    mergeServiceFiles()
    manifest {
        attributes(
            "mainClassName" to mainClassName,
            "Implementation-Version" to version.toString()
        )
    }
}