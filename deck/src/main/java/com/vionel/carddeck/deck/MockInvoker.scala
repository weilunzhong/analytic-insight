package com.vionel.carddeck.deck

import com.vionel.carddeck.deck.cardwriting.stories.Mocked
import com.vionel.carddeck.deck.datainterface._
import com.vionel.carddeck.deck.evaluation.{CardEvaluator, UCI}
import com.vionel.carddeck.deck.io.IO

/***
  * Generate mocked cards
  */
object MockInvoker {

    def main(args: Array[String]): Unit = {

        val cards = activeCards(userContentInteractions=List())

        println(IO.objectMapper().writeValueAsString(cards))
    }

    private def activeCards(
        userContentInteractions: List[UCI[User, Content, Interaction]]
    ): List[Card] = {
        new CardEvaluator[User, Content, Interaction]()
            .evaluate(
                userContentInteractions,
                cardWriters = List(
                    Mocked.mock
                )
            )
    }

}
