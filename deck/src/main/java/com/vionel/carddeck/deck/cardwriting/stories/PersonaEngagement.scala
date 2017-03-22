package com.vionel.carddeck.deck.cardwriting.stories

import com.vionel.carddeck.deck.datainterface._
import com.vionel.carddeck.deck.cardwriting._
import com.vionel.carddeck.deck.evaluation._
import com.vionel.carddeck.deck.cardwriting.Utils._


object PersonaEngagement {

    def personaEngagement(
        ucis: List[UCI[User, Content, Interaction]]
    ): List[CardTrigger] = {

        val latestInteractionTime = findLatestInteractionTime(ucis)

        val tooLowFinishedContentPercentage = 75
        val fromTime = latestInteractionTime.minusMonths(3)
        val toTime = latestInteractionTime

        val personasWithCompletion = ucis
            .filter(uci => isBetween(uci.interaction.startTime, from=fromTime, to=toTime))
            .groupBy(uci => uci.user.persona)
            .map{ case (persona, interactions) =>
                (persona, averageInteractionPercentFinished(interactions))
            }

        def cardTrigger(persona: String, averageFinishedContent: Double): CardTrigger = {
            val personaName = persona.capitalize
            val percentFinished = Math.round(averageFinishedContent)
            val description =
                s"The users of persona '$personaName' finish $percentFinished% of titles they start on average"
            val card = new Card(
                name = s"Low content engagement for persona ‘$personaName'",
                description = description,
                category = ReportCategory,
                headEntity = new BigSmallStatement(
                    bigPart = s"$percentFinished%",
                    smallPart = s"titles finished on average for persona '$personaName'",
                    concept = UserConcept
                ),
                body = new Body(
                    entities=List(
                        new PointLabelsRange(
                            minValue = 0,
                            maxValue = 100,
                            pointLabels = List(
                                new PointLabel[Percentage](
                                    s"$personaName",
                                    new Percentage(averageFinishedContent)
                                )
                            )
                        )
                    )
                )
            )
            new CardTrigger(
                averageFinishedContent < tooLowFinishedContentPercentage,
                card
            )
        }

        personasWithCompletion.map{
            case (persona, averageFinishedContent) => cardTrigger(persona, averageFinishedContent)
        }.toList
    }

}
