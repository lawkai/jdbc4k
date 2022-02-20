import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.gorylenko.gradle-git-properties") version "2.3.1"
    id("com.diffplug.spotless") version "6.2.1"
}

group = "com.github.lawkai"

repositories {
    mavenCentral()
}

kotlin.sourceSets.test {
    languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
}

val kotlinx_version = "1.6.0"
val slf4j_version = "1.7.36"
val logback_version = "1.2.10"
val hikari_version = "5.0.1"
val hsqldb_version = "2.6.1"
val hamkrest_version = "2.2"

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$kotlinx_version"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    // logging
    implementation("org.slf4j:slf4j-api:$slf4j_version")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("com.zaxxer:HikariCP:$hikari_version")
    testImplementation("org.hsqldb:hsqldb:$hsqldb_version")

    testImplementation("org.hamcrest:hamcrest:$hamkrest_version")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
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
