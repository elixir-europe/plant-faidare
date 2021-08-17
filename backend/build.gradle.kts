import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.springframework.boot.gradle.tasks.buildinfo.BuildInfo
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

plugins {
    java
    jacoco
    id("org.springframework.boot") version "2.1.2.RELEASE"
    id("com.gorylenko.gradle-git-properties") version "1.5.2"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("org.sonarqube")
    id("org.owasp.dependencycheck") version "6.0.3"
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

val snippetsDir = file("build/generated-snippets")

tasks {

    withType(JavaCompile::class.java) {
        // make sur the parameter names are writtn in the byte code and available using reflection.
        // this is useful for Jackson and Spring to automatically deduce propert names or path variable names
        // based on the name of the parameter
        options.compilerArgs.add("-parameters")
    }

    getByName<Copy>("processResources") {
        inputs.property("app", "gnpis")

        filesMatching("bootstrap.yml") {}
    }

    // this task is always out-of-date because it generates a properties file with the build time inside
    // so the bootJar task is also always out of date, too, since it depends on it
    // but it's better to do that than using the bootInfo() method of the springBoot closure, because that
    // makes the test task out of date, which makes the build much longer.
    // See https://github.com/spring-projects/spring-boot/issues/13152
    val buildInfo by creating(BuildInfo::class) {
        destinationDir = file("$buildDir/buildInfo")
    }

    val bootJar by getting(BootJar::class) {
        archiveName = "${rootProject.name}.jar"
        dependsOn(":frontend:assemble")
        dependsOn(buildInfo)

        into("BOOT-INF/classes/static") {
            from("${project(":frontend").projectDir}/dist/frontend")
        }
        into("BOOT-INF/classes/META-INF") {
            from(buildInfo.destinationDir)
        }
        launchScript()
    }

    val test by getting(Test::class) {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
        outputs.dir(snippetsDir)
    }

    val jacocoTestReport by getting(JacocoReport::class) {
        reports {
            xml.setEnabled(true)
            html.setEnabled(true)
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Finchley.SR1")
    }
}

dependencies {
    // Spring
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    // Elasticsearch
    implementation("org.elasticsearch:elasticsearch:6.6.2")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:6.6.2")

    // Swagger
    implementation("io.swagger:swagger-annotations:1.5.21")
    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")

    // Others
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("com.opencsv:opencsv:4.4")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.mockito:mockito-junit-jupiter:2.23.0")
    testImplementation("org.junit-pioneer:junit-pioneer:0.3.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
