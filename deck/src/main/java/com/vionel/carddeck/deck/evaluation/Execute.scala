package com.vionel.carddeck.deck.evaluation

import java.io.File

import com.vionel.carddeck.deck.cardwriting.stories.{Experiment, PersonaEngagement}
import com.vionel.carddeck.deck.datainterface.{Content, Interaction, User}
import com.vionel.carddeck.deck.io.IO


object Execute {

    // Card writer functions to be executed
    private val cardWriters: List[(List[UCI[User, Content, Interaction]]) => List[CardTrigger]] = List(
        Experiment.moviesWithManyViewsButLowFinishPercent,
        Experiment.leavers,
        Experiment.contentPerMonth
    )

    /***
      * @param interactionFile json file of format: one InteractionType implementation per line
      * @param userFile json file of format: one UserType implementation per line
      * @param contentFile json file of format: one ContentType implementation per line
      * @param cardsOutputFile json file of format: List[Card]
      */
    def generateCards(
        interactionFile: File,
        userFile: File,
        contentFile: File,
        cardsOutputFile: File
    ): Unit = {

        val ucis: List[UCI[User, Content, Interaction]] = IO.readUCIs(
            interactionFile = interactionFile,
            userFile = userFile,
            contentFile = contentFile
        )

        val cards = new CardEvaluator[User, Content, Interaction]().evaluate(ucis, cardWriters)

        IO.writeCards(cardsOutputFile, cards)
    }

}
