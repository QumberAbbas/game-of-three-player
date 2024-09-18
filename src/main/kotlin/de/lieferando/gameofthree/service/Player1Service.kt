package de.lieferando.gameofthree.service

import de.lieferando.gameofthree.communication.GameNumberSender
import de.lieferando.gameofthree.messaging.events.GameMessageEvent.GameMoveEvent
import de.lieferando.gameofthree.messaging.events.GameMessageEvent.GameOverEvent
import de.lieferando.gameofthree.messaging.events.GameMessageEvent.StartGameEvent
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
internal class Player1Service(
    private val gameMessageSender: GameNumberSender
) {
    private val logger = KotlinLogging.logger {}
    private val playerId = "Player1"

    fun startGame() {
        val gameStartRequestMsg = StartGameEvent(playerId = playerId)
        gameMessageSender.sendMessage(gameStartRequestMsg)
        logger.info { "Player 1 sent start game request" }
    }

    internal fun handleGameMove(message: GameMoveEvent) {
        val number = message.number
        logger.info { "Player 1 handling game update: $number" }
        val adjustment = if (number % 3 == 0) 0 else if (number % 3 == 1) -1 else 1
        val newNumber = (number + adjustment) / 3
        logger.info { "Player 1 adjusted number by $adjustment. New number: $newNumber" }

        val nextMove = GameMoveEvent(playerId, newNumber)
        gameMessageSender.sendMessage(nextMove)
        logger.info { "Player 1 sent new number $newNumber to Player 2" }
    }

    internal fun handleGameOver(message: GameOverEvent) {
        logger.info { "${message.winnerId} wins" }
    }
}
