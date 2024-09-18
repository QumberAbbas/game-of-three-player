package de.lieferando.gameofthree

import de.lieferando.gameofthree.controller.GameController
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class GameOfThreePlayerApplication(private val gameController: GameController) : CommandLineRunner {
    override fun run(vararg args: String?) {
        gameController.run()
    }
}

fun main(args: Array<String>) {
    runApplication<GameOfThreePlayerApplication>(*args)
}
