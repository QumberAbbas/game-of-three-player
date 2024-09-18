package de.lieferando.gameofthree.controller

import de.lieferando.gameofthree.service.Player1Service
import org.springframework.stereotype.Controller

@Controller
internal class GameController(private val player1Service: Player1Service) {

    companion object {
        private const val START_GAME_OPTION = 1
    }

    fun run(vararg args: String?) {
        println("Welcome to the Game of Three!")
        println("Press $START_GAME_OPTION to start the game")
        waitForGameStart()
        player1Service.startGame()
    }

    private fun waitForGameStart() {
        var gameStarted = false
        while (!gameStarted) {
            when (readUserInput()) {
                START_GAME_OPTION -> gameStarted = true
                else -> println("Invalid input. Please try again.")
            }
        }
    }

    private fun readUserInput(): Int? {
        val input = readlnOrNull()?.toIntOrNull()
        return input
    }
}
