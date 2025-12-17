import com.github.gradle.node.pnpm.task.PnpmInstallTask
import com.github.gradle.node.pnpm.task.PnpmTask
import web.versionsFromPackageJson

plugins {
    base
    id("com.github.node-gradle.node") version "7.1.0"
}

val versions = versionsFromPackageJson(file("package.json"))

node {
    version.set(versions.node)
    pnpmVersion.set(versions.pnpm)
    download.set(true)
}

tasks {
    npmInstall {
        enabled = false
    }

    val prepare by registering {
        dependsOn(PnpmInstallTask.NAME)
    }

    // This is not a pnpm_build task because the task to run is `pnpm build:prod`
    // and tasks with colons are not supported
    val pnpmBuildProd by registering(PnpmTask::class) {
        args.set(listOf("run", "build:prod"))
        dependsOn(prepare)
        inputs.file("webpack.config.js")
        inputs.file("tsconfig.json")
        inputs.file("package.json")
        inputs.file("pnpm-lock.yaml")
        inputs.dir("src")
        outputs.dir(layout.buildDirectory.dir("dist"))
    }

    assemble {
        dependsOn(pnpmBuildProd)
    }
}
