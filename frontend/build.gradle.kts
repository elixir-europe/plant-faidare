import com.moowork.gradle.node.npm.NpmTask

plugins {
    base
    id("com.moowork.node") version "1.2.0"
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

    // Unit tests
    val test by creating {
        if (isCi) {
            dependsOn("npm_run_test-ci")
        } else {
            dependsOn("npm_run_test")
        }
    }

    // E2E tests
    val clientIntegrationTest by creating {
        dependsOn("npm_run_e2e")
    }

    // Run lint before tests
    test.dependsOn("npm_run_lint")
    test.mustRunAfter("npm_run_lint")
    clientIntegrationTest.dependsOn("npm_run_lint")
    clientIntegrationTest.mustRunAfter("npm_run_lint")

    val check by getting {}
    check.dependsOn(test)

    val assemble by getting {}

    // Build
    val npm_run_build by getting {}
    assemble.dependsOn("npm_run_lint")
    assemble.dependsOn(npm_run_build)
    npm_run_build.mustRunAfter("npm_run_lint")

}

