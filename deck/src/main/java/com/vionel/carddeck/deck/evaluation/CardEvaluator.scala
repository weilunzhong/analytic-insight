package com.vionel.carddeck.deck.evaluation

import com.vionel.carddeck.deck.datainterface._

class CardEvaluator[A <: UserType, B <: ContentType, C <: InteractionType] {

    def evaluate(
        ucis: List[UCI[A, B, C]],
        cardWriters: List[(List[UCI[A, B, C]]) => List[CardTrigger]]
    ): List[Card] = cardWriters
        .par
        .flatMap(_.apply(ucis))
        .filter(cardTrigger => cardTrigger.triggered)
        .map(cardTrigger => cardTrigger.card)
        .toList
}

class CardTrigger(
    val triggered: Boolean,
    val card: Card
)

trait UCI[A <: UserType, B <: ContentType, C <: InteractionType] {
    val user: A
    val content: B
    val interaction: C
}
trait UserType {
    val id: String
}
trait ContentType {
    val id: String
}
trait InteractionType {
    val userID: String
    val contentID: String
}