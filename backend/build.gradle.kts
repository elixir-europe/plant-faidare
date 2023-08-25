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
    id("org.springframework.boot") version "2.7.4"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.sonarqube")
    id("org.owasp.dependencycheck") version "7.2.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
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
        destinationDir = file("$buildDir/buildInfo")
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

extra["springCloudVersion"] = "2021.0.4"
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    constraints {
        implementation("org.apache.logging.log4j:log4j-core") {
            version {
                strictly("[2.17, 3[")
                prefer("2.17.0")
            }
            because("CVE-2021-44228, CVE-2021-45046, CVE-2021-45105: Log4j vulnerable to remote code execution and other critical security vulnerabilities")
        }
    }
    // Spring
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Elasticsearch
    implementation("org.elasticsearch:elasticsearch")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")

    // Others
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.opencsv:opencsv:5.7.0")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jsoup:jsoup:1.15.3")
}
