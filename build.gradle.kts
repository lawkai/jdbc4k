import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
    id("com.gorylenko.gradle-git-properties") version "2.3.1"
    id("com.diffplug.spotless") version "6.2.1"
    `maven-publish`
    signing
}

group = "io.github.lawkai"

repositories {
    mavenCentral()
}

val kotlinx_version = "1.6.0"
val slf4j_version = "1.7.36"
val logback_version = "1.2.11"
val hikari_version = "5.0.1"
val hsqldb_version = "2.6.1"
val hamkrest_version = "1.8.0.1"

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
    testRuntimeOnly("org.hsqldb:hsqldb:$hsqldb_version")
    testRuntimeOnly("mysql:mysql-connector-java:8.0.28")

    testImplementation("com.natpryce:hamkrest:$hamkrest_version")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
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

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("javadoc"))
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenArtifacts") {
            from(components["kotlin"])
            artifact(sourcesJar)
            artifact(javadocJar)

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set("JDBC4k")
                description.set("A simple Kotlin Library that provides suspendable JDBC transaction and query.")
                url.set("https://github.com/lawkai/jdbc4k")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://mit-license.org/")
                    }
                }

                scm {
                    connection.set("scm:git:git@github.com:lawkai/jdbc4k.git")
                    developerConnection.set("scm:git:git@github.com:lawkai/jdbc4k.git")
                    url.set("https://github.com/lawkai/jdbc4k")
                }

                developers {
                    developer {
                        id.set("lawkai")
                        name.set("Kelvin Law")
                    }
                }
            }
        }

        repositories {
            maven {
                val stagingUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")

                url = if (version.toString().contains("SNAPSHOT")) snapshotUrl else stagingUrl
                credentials {
                    username = listOf(
                        project.properties["ossrh.username"]?.toString(),
                        System.getenv("OSSRH_USERNAME")?.toString(),
                    ).filterNotNull().firstOrNull()
                    password = listOf(
                        project.properties["ossrh.password"]?.toString(),
                        System.getenv("OSSRH_PASSWORD")?.toString(),
                    ).filterNotNull().firstOrNull()
                }
            }
        }
    }
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    if (signingKey != null && signingPassword != null) {
        if (signingKeyId != null) {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        } else {
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
    }
    sign(publishing.publications["mavenArtifacts"])
}
