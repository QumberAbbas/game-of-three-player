package de.lieferando.gameofthree.messaging.events


sealed class GameMessageEvent {
    abstract val playerId: String // Common for all messages

    // Message type for starting the game
    data class StartGameEvent(
        override val playerId: String
    ) : GameMessageEvent()

    // Message type for game moves (number moves)
    data class GameMoveEvent(
        override val playerId: String,
        val number: Int
    ) : GameMessageEvent()

    // Message type for game over and winner declaration
    data class GameOverEvent(
        override val playerId: String,
        val winnerId: String
    ) : GameMessageEvent()
}