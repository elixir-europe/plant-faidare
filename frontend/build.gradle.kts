import com.moowork.gradle.node.npm.NpmTask

plugins {
    base
    id("com.moowork.node") version "1.2.0"
    id("org.sonarqube")
}

val isCi = System.getenv("CI") != null

node {
    version = "10.13.0"
    npmVersion = "6.4.1"
    if (isCi) {
        // we specify a custom installation directory because of permission issues on Docker
        workDir = file("/tmp/node")
    }
    download = true
}


tasks {

    // Lint
    val lint by creating {
        dependsOn("npm_run_lint")
    }

    // Unit tests
    val test by creating {
        if (isCi) {
            dependsOn("npm_run_test-multi-browsers")
        } else {
            dependsOn("npm_run_test")
        }
    }

    // E2E tests
    val clientIntegrationTest by creating {
        dependsOn("npm_run_e2e")
    }

    val check by getting {
        dependsOn(test)
        dependsOn(lint)
    }

    // Build assemble
    val assemble by getting {
        dependsOn("npm_run_build")
    }

}

