val exposed_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
}

group = "kate.tutorial.kotlin"
version = "0.0.1"
application {
    mainClass.set("kate.tutorial.kotlin.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")

    implementation("com.google.firebase:firebase-admin:8.0.0")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jodatime:$exposed_version")
    implementation("com.h2database:h2:1.4.200")
}
