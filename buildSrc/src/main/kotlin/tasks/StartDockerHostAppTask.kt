package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class StartDockerHostAppTask : DefaultTask() {
    init {
        group = "docker"
        description = "Starts the Docker host app"
    }

    // Execution code
    @TaskAction
    fun execute() {
        project.exec {
            commandLine("docker-compose", "run", "--rm", "-i", "app")
        }
    }
}