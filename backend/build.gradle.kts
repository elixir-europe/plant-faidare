import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.springframework.boot.gradle.tasks.buildinfo.BuildInfo

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

tasks {

    withType(JavaCompile::class.java) {
        // make sur the parameter names are writtn in the byte code and available using reflection.
        // this is useful for Jackson and Spring to automatically deduce propert names or path variable names
        // based on the name of the parameter
        options.compilerArgs.add("-parameters")
    }

    // this task is always out-of-date because it generates a properties file with the build time inside
    // so the bootJar task is also always out of date, too, since it depends on it
    // but it's better to do that than using the bootInfo() method of the springBoot closure, because that
    // makes the test task out of date, which makes the build much longer.
    // See https://github.com/spring-projects/spring-boot/issues/13152
    val buildInfo by registering(BuildInfo::class) {
        destinationDir.set(file(project.layout.buildDirectory.dir("buildInfo")))
    }

    bootJar {
        archiveFileName.set("${rootProject.name}.jar")
        dependsOn(buildInfo)
        dependsOn(":web:assemble")

        // replace the script.js and style.css file names referenced in main.html
        // by their actual name, containing the content hash
        filesMatching("**/layout/main.html") {
            val webAssetsDir = project(":web").file("build/dist/resources/");
            val scriptFileName = webAssetsDir.list().first { it.startsWith("script") && it.endsWith(".js") }
            val styleFileName = webAssetsDir.list().first { it.startsWith("style") && it.endsWith(".css") }

            filter { line ->
                if (line.contains("script.js")) {
                    line.replace("script.js", scriptFileName)
                }
                else if (line.contains("style.css")) {
                    line.replace("style.css", styleFileName)
                } else {
                    line
                }
            }
        }

        into("BOOT-INF/classes/META-INF") {
            from(buildInfo.map { it.destinationDir })
        }
        into("BOOT-INF/classes/static") {
            from(project(":web").file("build/dist"))
        }
        launchScript()
    }

    test {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

extra["springCloudVersion"] = "2022.0.4"
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    // Spring
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")

    implementation("org.springframework.cloud:spring-cloud-starter-config")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // Others
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.opencsv:opencsv:5.7.0")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jsoup:jsoup:1.15.3")
}
