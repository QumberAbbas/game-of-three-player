package de.lieferando.gameofthree.messaging.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.ShutdownSignalException
import mu.KotlinLogging
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.support.RetryTemplate


@Configuration
@EnableRabbit
internal class GamesOfThreeRabbitMQConfig {

    private val logger = KotlinLogging.logger {}

    @Bean(GAME_EXCHANGE)
    fun gameTopicExchange() = DirectExchange(GAME_EXCHANGE)

    fun player1Queue(): Queue {
        return Queue(/* name = */ PLAYER_1_QUEUE, /* durable = */
            true, /* exclusive = */
            false, /* autoDelete = */
            true
        )
    }

    fun player2Queue(): Queue {
        return Queue(/* name = */ PLAYER_2_QUEUE, /* durable = */
            true, /* exclusive = */
            false, /* autoDelete = */
            true
        )
    }

    @Bean
    fun bindingPlayer1Queue(): Binding {
        return BindingBuilder.bind(player1Queue()).to(gameTopicExchange()).with(PLAYER1_ROUTING)
    }

    @Bean
    fun bindingPlayer2Queue(): Binding {
        return BindingBuilder.bind(player2Queue()).to(gameTopicExchange()).with(PLAYER2_ROUTING)
    }

    @Bean
    fun backOffPolicy(): ExponentialBackOffPolicy {
        return ExponentialBackOffPolicy().apply {
            initialInterval = 500
            multiplier = 10.0
            maxInterval = 10000
        }
    }

    @Bean
    fun retryTemplate(backOffPolicy: ExponentialBackOffPolicy): RetryTemplate {
        return RetryTemplate().apply {
            this.setBackOffPolicy(backOffPolicy)
        }
    }

    @Bean
    fun jackson2JsonMessageConverter(jsonMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(jsonMapper)
    }

    @Bean("gotConnectionFactory")
    fun gotConnectionFactory(): ConnectionFactory {
        return CachingConnectionFactory("rabbitmq-container", 5672).apply {
            username = "guest"
            this.setPassword("guest")
            addConnectionListener(object : ConnectionListener {
                override fun onCreate(connection: Connection) {
                }

                override fun onShutDown(signal: ShutdownSignalException) {
                    super.onShutDown(signal)
                    logger.error { "ConnectionFactory shutdown with reason $signal.reason" }
                }
            })
        }
    }

    @Bean("gotRabbitTemplate")
    fun gotRabbitTemplate(
        @Qualifier("gotConnectionFactory") connectionFactory: ConnectionFactory,
        jackson2JsonMessageConverter: MessageConverter
    ): RabbitTemplate {
        return RabbitTemplate(connectionFactory).apply {
            messageConverter = jackson2JsonMessageConverter
            setConfirmCallback { correlationData, ack, cause ->
                if (ack) {
                    logger.info { "Message sent successfully with correlation id $correlationData" }
                } else {
                    logger.error { "Message failed to send with correlation id $correlationData and reason $cause" }
                }
            }
        }
    }

    @Bean
    fun container(connectionFactory: ConnectionFactory, queue: List<Queue>): SimpleMessageListenerContainer {
        return SimpleMessageListenerContainer().apply {
            this.connectionFactory = connectionFactory
            this.setQueueNames(PLAYER_1_QUEUE, PLAYER_2_QUEUE)
        }
    }

    internal companion object {
        const val GAME_EXCHANGE = "game.exchange"

        const val PLAYER_1_QUEUE = "player1.queue"
        const val PLAYER_2_QUEUE = "player2.queue"

        const val PLAYER1_ROUTING = "player1"
        const val PLAYER2_ROUTING = "player2"
    }
}