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

        // Check if a command-line argument is passed
        if (args.isNotEmpty() && args[0] == START_GAME_OPTION.toString()) {
            startGame()
        } else {
            println("Press $START_GAME_OPTION to start the game")
            waitForGameStart()
            startGame()
        }
    }

    private fun waitForGameStart() {
        var gameStarted = false
        while (!gameStarted) {
            val userInput = readUserInput()
            if (userInput == null) {
                println("Invalid input. Please enter a valid number.")
            } else {
                when (userInput) {
                    START_GAME_OPTION -> gameStarted = true
                    else -> println("Invalid input. Please try again.")
                }
            }
        }
    }

    private fun readUserInput(): Int? {
        val input = readlnOrNull()
        return try {
            input?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun startGame() {
        player1Service.startGame()
    }
}