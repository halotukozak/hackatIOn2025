val exposed_version: String by project
val h2_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
  kotlin("jvm") version "2.1.20"
  id("io.ktor.plugin") version "3.1.2"
  id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
}

group = "edu.agh.roomie"
version = "0.0.1"

application {
  mainClass = "io.ktor.server.netty.EngineMain"

  val isDevelopment: Boolean = project.ext.has("development")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.register("exportRestModels", JavaExec::class) {
  group = "application"
  description = "Generate TypeScript models from Kotlin data classes"
  classpath = sourceSets["main"].runtimeClasspath
  mainClass.set("edu.agh.roomie.scripts.ExportRestModelsKt")
  // Default target directory is "../frontend/src/rest" relative to the project root
  args = listOf("../frontend/src/rest")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.ktor:ktor-server-core")
  implementation("io.ktor:ktor-server-auth")
  implementation("io.ktor:ktor-client-core")
  implementation("io.ktor:ktor-client-apache")
  implementation("io.ktor:ktor-server-host-common")
  implementation("io.ktor:ktor-server-status-pages")
  implementation("io.ktor:ktor-server-compression")
  implementation("io.ktor:ktor-server-cors")
  implementation("io.ktor:ktor-server-default-headers")
  implementation("io.ktor:ktor-server-openapi")
  implementation("io.ktor:ktor-server-swagger")
  implementation("io.ktor:ktor-server-content-negotiation")
  implementation("io.ktor:ktor-serialization-kotlinx-json")
  implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
  implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
  implementation("com.h2database:h2:$h2_version")
  implementation("io.ktor:ktor-server-netty")
  implementation("ch.qos.logback:logback-classic:$logback_version")
  implementation("io.ktor:ktor-server-config-yaml")
  implementation("dev.adamko.kxstsgen:kxs-ts-gen-core:0.2.1")
  testImplementation("io.ktor:ktor-server-test-host")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
