plugins {
    id("org.sonarqube") version "3.2.0"
}

sonarqube {
    properties {
        property ("sonar.projectKey", "urgi-is_faidare_AXlGu_BxPgTGgvpuDgeB")
        property ("sonar.qualitygate.wait", false)
    }
}
