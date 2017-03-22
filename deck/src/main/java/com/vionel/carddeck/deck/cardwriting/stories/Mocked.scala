package com.vionel.carddeck.deck.cardwriting.stories

import com.vionel.carddeck.deck.datainterface._
import com.vionel.carddeck.deck.cardwriting._
import com.vionel.carddeck.deck.evaluation.{UCI, CardTrigger}
import com.vionel.carddeck.deck.cardwriting.UserConcept
import org.joda.time.DateTime


object Mocked {

    def mock(
        ucis: List[UCI[User, Content, Interaction]]
    ): List[CardTrigger] = {

        val mockedReferenceTime = DateTime.now()

        val mockedCards = List(
            new Card(
                name = "title",
                description = "description",
                category = ReportCategory,
                headEntity = new BigSmallStatement(
                    bigPart = "Big part!",
                    smallPart = "small part :)",
                    concept = UserConcept
                ),
                body = new Body(
                    entities = List(
                        new BarPlot(
                            name = "title",
                            subtitle = "subtitle",
                            xLabel = "xLabel",
                            yLabel = "yLabel",
                            yScale = LinearScale,
                            groups = List(
                                new DataGroup(
                                    name = "group A",
                                    values = Range(0, 10).toList.map(
                                        i => new XY(s"bucket $i", 100 * Math.random())
                                    )
                                ),
                                new DataGroup(
                                    name = "group B",
                                    values = Range(0, 10).toList.map(
                                        i => new XY(s"bucket $i", 20 * Math.random())
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            new Card(
                name = "title",
                description = "description",
                category = ReportCategory,
                headEntity = new BigSmallStatement(
                    bigPart = "Other big part!",
                    smallPart = "small part",
                    concept = UserConcept
                ),
                body = new Body(
                    entities = List(
                        new DateLinePlot(
                            name = "title",
                            subtitle = "subtitle",
                            xLabel = "xLabel",
                            yLabel = "yLabel",
                            yScale = LinearScale,
                            lines = List(
                                new DataGroup(
                                    name = "line A",
                                    values = List(
                                        new XY(
                                            x = new Datetime(mockedReferenceTime.minusMonths(5)),
                                            y = 1
                                        ),
                                        new XY(
                                            x = new Datetime(mockedReferenceTime.minusMonths(4)),
                                            y = 2
                                        ),
                                        new XY(
                                            x = new Datetime(mockedReferenceTime.minusMonths(3)),
                                            y = 3.5
                                        ),
                                        new XY(
                                            x = new Datetime(mockedReferenceTime.minusMonths(2)),
                                            y = 4
                                        ),
                                        new XY(
                                            x = new Datetime(mockedReferenceTime.minusMonths(1)),
                                            y = 3
                                        ),
                                        new XY(
                                            x = new Datetime(mockedReferenceTime),
                                            y = 7
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            new Card(
                name = "title",
                description = "description",
                category = ReportCategory,
                headEntity = new BigSmallStatement(
                    bigPart = "Other big part!",
                    smallPart = "small part",
                    concept = UserConcept
                ),
                body = new Body(
                    entities = List(
                        new NumericLinePlot(
                            name = "title",
                            subtitle = "subTitle",
                            xLabel = "xLabel",
                            yLabel = "yLabel",
                            xScale = LinearScale,
                            yScale = LinearScale,
                            lines = List(
                                new DataGroup(
                                    name = "line A",
                                    values = Range(0, 10).toList.map(
                                        i => new XY(
                                            x = i.toDouble,
                                            y = Math.random()
                                        )
                                    )
                                ),
                                new DataGroup(
                                    name = "line B",
                                    values = Range(0, 15).toList.map(
                                        i => new XY(
                                            x = i.toDouble,
                                            y = Math.random()
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        mockedCards.map(card => new CardTrigger(triggered = true, card))
    }

}
