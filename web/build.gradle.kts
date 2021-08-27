import com.github.gradle.node.yarn.task.YarnInstallTask
import com.github.gradle.node.yarn.task.YarnTask

plugins {
    base
    id("com.github.node-gradle.node") version "3.0.1"
}

node {
    version.set("14.17.0")
    npmVersion.set("6.14.10")
    yarnVersion.set("1.22.10")
    download.set(true)
}

tasks {
    npmInstall {
        enabled = false
    }

    val prepare by registering {
        dependsOn(YarnInstallTask.NAME)
    }

    // This is not a yarn_build task because the task to run is `yarn build:prod`
    // and tasks with colons are not supported
    val yarnBuildProd by registering(YarnTask::class) {
        args.set(listOf("run", "build:prod"))
        dependsOn(prepare)
        inputs.file("webpack.common.js")
        inputs.file("webpack.prod.js")
        inputs.file("tsconfig.json")
        inputs.file("package.json")
        inputs.file("yarn.lock")
        inputs.dir("src")
        outputs.dir("$buildDir/dist")
    }

    assemble {
        dependsOn(yarnBuildProd)
    }
}
