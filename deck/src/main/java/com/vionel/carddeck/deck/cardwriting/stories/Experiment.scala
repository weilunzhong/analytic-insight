package com.vionel.carddeck.deck.cardwriting.stories

import com.vionel.carddeck.deck.cardwriting.{LinearScale, ReportCategory, UserConcept}
import com.vionel.carddeck.deck.cardwriting.Utils._
import com.vionel.carddeck.deck.datainterface._
import com.vionel.carddeck.deck.evaluation._
import org.joda.time.Days


object Experiment {

    def contentPerMonth(
        ucis: List[UCI[User, Content, Interaction]]
    ): List[CardTrigger] = {

        val contentPerMonth = ucis
          .groupBy(uci => uci.user)
          .filter{ case (user, user_ucis) => Days.daysBetween(user.firstActivity, user.lastActivity).getDays >= APPROXIMATE_MONTH_IN_DAYS}
          .map{ case (user, user_ucis) =>
              uniqueContents(user_ucis.filter(contentFinished)).size.toDouble / Days.daysBetween(user.firstActivity, user.lastActivity).getDays
          }
          .map(contentsPerDays => contentsPerDays * APPROXIMATE_MONTH_IN_DAYS)

        val histogram = contentPerMonth
          .groupBy{
                case perMonth if perMonth < 1 => "up to one"
                case perMonth if perMonth >= 1 && perMonth < 2 => "up to two"
                case perMonth if perMonth >= 2 && perMonth < 4 => "up to four"
                case perMonth if perMonth >= 4 => "more than four"
          }
          .map{ case (bucketName, occurrences) => (occurrences.max, bucketName, occurrences.size)}
          .toList
          .sortBy{ case (perMonth, bucketName, numUsers) => perMonth}
          .map{ case (perMonth, bucketName, numUsers) => (bucketName, numUsers.toDouble)}

        List(
            new CardTrigger(
                triggered = true,
                card = new Card(
                    name = "Content completed per month",
                    description = "Average number of content completed by users",
                    category = ReportCategory,
                    headEntity = new BigSmallStatement(
                        bigPart = "User activity",
                        smallPart = "Content completed per month",
                        concept = UserConcept
                    ),
                    body = new Body(
                        entities = List(
                            new BarPlot(
                                name = "name",
                                subtitle = "subtitle",
                                xLabel = "content completed per month",
                                yLabel = "users",
                                yScale = LinearScale,
                                groups = List(
                                    new DataGroup(
                                        name = "name",
                                        values = histogram.map{
                                            case (bucketName, value) => new XY(bucketName, value.toDouble)
                                        }
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    def leavers(
        ucis: List[UCI[User, Content, Interaction]]
    ): List[CardTrigger] = {

        val latestInteractionTime = findLatestInteractionTime(ucis)
        val inactivityThresholdInMonths = 2
        val inactivityThresholdInDays = APPROXIMATE_MONTH_IN_DAYS * inactivityThresholdInMonths
        val leaverInactivityThreshold = latestInteractionTime.minusDays(inactivityThresholdInDays)

        val leavers = ucis
          .map(uci => uci.user)
          .distinct
          .filter(user => isBefore(user.lastActivity, leaverInactivityThreshold))

        val withinADayLeaverSet = leavers
          .filter(user => daysBetweenFirstAndLastInteraction(user) < 1)
          .toSet

        val aDayOrMoreLeaverSet = leavers
          .filter(user => daysBetweenFirstAndLastInteraction(user) >= 1)
          .toSet

        val nonLeaverSet = ucis
          .map(uci => uci.user)
          .distinct
          .filter(user => isAfter(user.lastActivity, leaverInactivityThreshold) && daysBetweenFirstAndLastInteraction(user) > inactivityThresholdInDays)
          .toSet

        val daysBeforeLeavingHistogram = histogram(
            leavers.map(user => Days.daysBetween(user.firstActivity, user.lastActivity).getDays.toDouble),
            {
                case days if days < 1 => "a day"
                case days if days >= 1 && days < 7 => "a week"
                case days if days >= 7 && days < 30 => "a month"
                case days if days >= 30 && days < 182 => "six months"
                case days if days >= 182 => "more than six months"
            }
        )

        val contentCompletion = (userInteractions: List[UCI[User, Content, Interaction]]) =>
            userInteractions.count(contentFinished).toDouble / userInteractions.size

        val withinADayLeaverNumStartedContent = averagePerUser[Content, Interaction](
            ucis,
            (user) => withinADayLeaverSet(user),
            (userInteractions) => userInteractions.size.toDouble
        )
        val withinADayLeaverContentCompletion = toPercent(averagePerUser[Content, Interaction](
            ucis,
            (user) => withinADayLeaverSet(user),
            contentCompletion)
        )
        val aDayOrMoreLeaverContentCompletion = toPercent(averagePerUser[Content, Interaction](
            ucis,
            (user) => aDayOrMoreLeaverSet(user),
            contentCompletion)
        )
        val nonLeaverContentCompletion = toPercent(averagePerUser[Content, Interaction](
            ucis,
            (user) => nonLeaverSet(user),
            contentCompletion)
        )

        println(withinADayLeaverContentCompletion)
        println(aDayOrMoreLeaverContentCompletion)
        println(nonLeaverContentCompletion)
        println(withinADayLeaverNumStartedContent)

        List(
            new CardTrigger(
                triggered = true,
                card = new Card(
                    name = "Time until leavers leave",
                    description = "Below you can see the time between leavers first and last activity in the service. Leavers are " +
                      s"set to be users who have been inactive for more than $inactivityThresholdInMonths months.",
                    category = ReportCategory,
                    headEntity = new BigSmallStatement(
                        bigPart = "Leavers",
                        smallPart = "Time before leaving",
                        concept = UserConcept
                    ),
                    body = new Body(
                        entities = List(
                            new BarPlot(
                                name = "name",
                                subtitle = "subtitle",
                                xLabel = "Left within",
                                yLabel = "% of users that left",
                                yScale = LinearScale,
                                groups = List(
                                    new DataGroup(
                                        name = "name",
                                        values = daysBeforeLeavingHistogram.map{
                                            case (bucketName, value) => new XY(bucketName, value.toDouble)
                                        }
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    def moviesWithManyViewsButLowFinishPercent(
        ucis: List[UCI[User, Content, Interaction]]
    ): List[CardTrigger] = {

        val contentViews = ucis.groupBy(uci => uci.content.id).map { case (content_id, content_ucis) => content_ucis.size }
        val averageContentViews = contentViews.sum.toDouble / contentViews.size

        ucis
          .groupBy(uci => uci.content)
          .filter { case (content, content_ucis) => content_ucis.size > averageContentViews }
          .map { case (content, content_ucis) =>
              (content.metaData.name, content.metaData.releaseYear, content_ucis.size, averageInteractionPercentFinished(content_ucis))
          }
          .toList
          .sortBy{ case (_, _, _, finishPercent) => finishPercent}
//          .foreach(println(_))

        List()
    }

}
