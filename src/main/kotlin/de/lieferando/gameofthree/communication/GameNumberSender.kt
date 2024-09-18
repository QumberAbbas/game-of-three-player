package de.lieferando.gameofthree.communication

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.lieferando.gameofthree.messaging.config.GamesOfThreeRabbitMQConfig.Companion.GAME_EXCHANGE
import de.lieferando.gameofthree.messaging.config.GamesOfThreeRabbitMQConfig.Companion.PLAYER2_ROUTING
import de.lieferando.gameofthree.messaging.events.GameMessageEvent
import mu.KotlinLogging
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
internal class GameNumberSender(private val rabbitTemplate: RabbitTemplate) {

    private val logger = KotlinLogging.logger {}

    fun sendMessage(gameMessage: GameMessageEvent) {
        val mapper = jacksonObjectMapper()
        val messageString = mapper.writeValueAsString(gameMessage)
        rabbitTemplate.convertAndSend(GAME_EXCHANGE, PLAYER2_ROUTING, messageString)
        logger.info { "Sent message to system: $messageString" }
    }
}