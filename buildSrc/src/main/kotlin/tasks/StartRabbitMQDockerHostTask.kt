package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class StartRabbitMQDockerHostTask : DefaultTask() {
    init {
        group = "docker"
        description = "Starts the Rabbit MQ Docker host app"
    }

    // Execution code
    @TaskAction
    fun execute() {
        project.exec {
            commandLine("docker-compose", "-f", "docker-compose.rabbit.yml", "up", "-d")
        }
    }
}