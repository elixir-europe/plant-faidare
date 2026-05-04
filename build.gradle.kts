plugins {
    id("org.sonarqube") version "7.3.0.8198"
}

sonarqube {
    properties {
        property ("sonar.projectKey", "urgi-is_faidare_AXlGu_BxPgTGgvpuDgeB")
        property ("sonar.qualitygate.wait", false)
        property("sonar.exclusions", "**/*.gradle.kts")
    }
}
