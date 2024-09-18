package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ExitDockerHostAppTask : DefaultTask() {
    init {
        group = "docker"
        description = "Exits the Docker host app"
    }
    // Execution code
    @TaskAction
    fun execute() {
        project.exec {
            commandLine("docker-compose", "down")
        }
    }
}