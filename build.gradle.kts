plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}

subprojects {
    plugins.apply("java")
    plugins.apply("com.github.johnrengelman.shadow")

    group = "me.davamu"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    tasks {
        compileJava {
            options.release.set(11)
        }

        assemble {
            dependsOn(shadowJar)
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        shadowJar {
            minimize()
        }
    }

}

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
    shadowJar {
        arrayOf("velocity").forEach {
            val buildTask = project(":$it").tasks.named("shadowJar")
            dependsOn(buildTask)
            from(zipTree(buildTask.map {out -> out.outputs.files.singleFile}))
        }
        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}