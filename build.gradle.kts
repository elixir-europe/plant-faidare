plugins {
    id("org.sonarqube") version "4.3.0.3225"
}

sonarqube {
    properties {
        property ("sonar.projectKey", "urgi/is_faidare_AXlGu_BxPgTGgvpuDgeB")
        property ("sonar.qualitygate.wait", false)
        property("sonar.exclusions", "**/*.gradle.kts")
    }
}
