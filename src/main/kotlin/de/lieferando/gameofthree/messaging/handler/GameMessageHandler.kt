package de.lieferando.gameofthree.messaging.handler

import de.lieferando.gameofthree.messaging.config.GamesOfThreeRabbitMQConfig.Companion.PLAYER_1_QUEUE
import de.lieferando.gameofthree.messaging.events.GameMessageEvent
import de.lieferando.gameofthree.messaging.events.GameMessageEvent.GameMoveEvent
import de.lieferando.gameofthree.messaging.events.GameMessageEvent.GameOverEvent
import de.lieferando.gameofthree.service.Player1Service
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
internal class GameMessageHandler(private val gameService: Player1Service) {
    private val logger = KotlinLogging.logger {}

    @RabbitListener(queues = [PLAYER_1_QUEUE])
    fun receiveMessageFromPlayer2(message: GameMessageEvent) {
        println("Player 1 received message from Player 2: $message")

        when (message) {
            is GameMoveEvent -> gameService.handleGameMove(message)
            is GameOverEvent -> gameService.handleGameOver(message)
            else -> logger.error { "Invalid message type: $message" }
        }
    }
}