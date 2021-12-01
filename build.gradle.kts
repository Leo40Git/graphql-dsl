plugins {
    kotlin("jvm") version "1.5.31"
    `maven-publish`
    signing
}

group = "io.github.speedbridgemc"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

val sourcesJar by tasks.registering(Jar::class) {
    group = "build"

    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    group = "build"

    dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
    archiveClassifier.set("javadoc")
    from(tasks["javadoc"])
}

val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

val ossrhUsername: String? by project
val ossrhPassword: String? by project

publishing {
    repositories {
        maven {
            val releaseRepo = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotRepo = "https://s01.oss.sonatype.org/content/repositories/snapshots/"

            url = uri(if (isReleaseVersion) releaseRepo else snapshotRepo)
            credentials {
                username = ossrhUsername ?: "Unknown user"
                password = ossrhPassword ?: "Unknown password"
            }
        }
    }

    publications {
        register("mavenJava", MavenPublication::class) {
            pom {
                from(components["java"])
                artifact(sourcesJar.get())
                artifact(javadocJar.get())

                name.set("GraphQL DSL")
                description.set("Provides a Kotlin DSL for composing GraphQL queries and mutations.")
                url.set("https://speedbridge.github.io/graphql-dsl")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }
                developers {
                    developer {
                        id.set("ADudeCalledLeo")
                        name.set("Kfir Awad")
                        email.set("kfirawad@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/SpeedbridgeMC/graphql-dsl.git")
                    developerConnection.set("scm:git:ssh://github.com:SpeedbridgeMC/graphql-dsl.git")
                    url.set("https://github.com/SpeedbridgeMC/graphql-dsl/tree/master")
                }
            }
        }
    }
}
