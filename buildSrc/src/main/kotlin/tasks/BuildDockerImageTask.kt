package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class BuildDockerImageTask : DefaultTask() {
    init {
        group = "docker"
        description = "Builds a Docker image"
    }

    // Execution code
    @TaskAction
    fun execute() {
        project.exec {
            commandLine("docker", "build", "-t", "game-of-3", "-f", "Dockerfile", ".")
        }
    }
}