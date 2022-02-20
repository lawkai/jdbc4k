import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
    id("com.gorylenko.gradle-git-properties") version "2.3.1"
    id("com.diffplug.spotless") version "6.2.1"
}

group = "com.github.lawkai"

repositories {
    mavenCentral()
}

val kotlinx_version = "1.6.0"
val slf4j_version = "1.7.36"
val logback_version = "1.2.10"

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$kotlinx_version"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // logging
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    testRuntimeOnly("ch.qos.logback:logback-classic:$logback_version")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

spotless {
    encoding("UTF-8")
    kotlin {
        ktlint().userData(
            mapOf(
                "disabled_rules" to "no-wildcard-imports",
                "ij_continuation_indent_size" to "4",
            ),
        )
        // configure in diktat-analysis.yml
        diktat()
        trimTrailingWhitespace()
    }
    kotlinGradle {
        ktlint()
        trimTrailingWhitespace()
    }
    json {
        target("src/**/*.json")
        simple()
        trimTrailingWhitespace()
    }
}

gitProperties { customProperties.put("project.name", project.name) }
