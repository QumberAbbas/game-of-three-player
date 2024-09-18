package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import tasks.BuildDockerImageTask
import tasks.ExitDockerHostAppTask
import tasks.StartDockerHostAppTask
import tasks.StartRabbitMQDockerHostTask

class GameOfThreePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register("buildDockerImage", BuildDockerImageTask::class)
            tasks.register<StartDockerHostAppTask>("run") {

            }
            tasks.register("runRabbitMQ", StartRabbitMQDockerHostTask::class)
            tasks.register("exit", ExitDockerHostAppTask::class)
        }
    }
}