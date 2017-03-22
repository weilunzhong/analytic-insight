package com.vionel.carddeck.deck.cardwriting

import com.vionel.carddeck.deck.datainterface.{Content, Interaction, User}
import com.vionel.carddeck.deck.evaluation.{ContentType, InteractionType, UCI}
import org.joda.time.{DateTime, Days}

object Utils {

    val APPROXIMATE_MONTH_IN_DAYS = 30

    def isBetween(dt: DateTime, from: DateTime, to: DateTime) = dt.isAfter(from) && dt.isBefore(to)

    def isBefore(dt: DateTime, to: DateTime) = dt.isBefore(to)

    def isAfter(dt: DateTime, to: DateTime) = dt.isAfter(to)

    def daysBetweenFirstAndLastInteraction(user: User) = Days.daysBetween(user.firstActivity, user.lastActivity).getDays

    def interactionDuration(uci: UCI[_, _, Interaction]) = uci.interaction.durationInSeconds

    def contentDuration(uci: UCI[_, Content, _]) = uci.content.metaData.durationInSeconds

    def uniqueContents(ucis: List[UCI[_, Content, _]]) = ucis.map(uci => uci.content).distinct

    def toPercent(relationalValue: Double) = math.round(100 * relationalValue).toInt

    def contentFinished(uci: UCI[_, Content, Interaction]) = {
        val contentFinishThreshold = 0.9
        (interactionDuration(uci) / contentDuration(uci)) > contentFinishThreshold
    }

    def averageInteractionPercentFinished(interactions: List[UCI[User, Content, Interaction]]) = {
        100d * average(interactions, (uci) => if (contentFinished(uci)) 1d else 0d)
    }

    def average(
        collection: List[UCI[User, Content, Interaction]],
        evaluator: (UCI[User, Content, Interaction]) => Double)
    = collection.foldLeft(0d)(_ + evaluator(_)) / collection.size

    def findLatestInteractionTime(interactions: List[UCI[_, _, Interaction]]): DateTime = {
        val latestInteraction = interactions.maxBy(uci => uci.interaction.startTime.getMillis)
        latestInteraction.interaction.startTime
    }

    def averagePerUser[B <: ContentType, C <: InteractionType](
        ucis: List[UCI[User, B, C]],
        userFilter: (User) => Boolean,
        userInteractionsEvaluator: (List[UCI[User, B, C]]) => Double
    ) = {

        val userInteractionGroups = ucis
          .filter(uci => userFilter(uci.user))
          .groupBy(uci => uci.user)

        val numUsers = userInteractionGroups.size

        userInteractionGroups
          .map{ case (user, user_ucis) => (user, userInteractionsEvaluator(user_ucis)) }
          .map{ case (_, numContentStarted) => numContentStarted }
          .sum / numUsers
    }

    def histogram(values: List[Double], bucketMapper: (Double) => String): List[(String, Int)] = {
        values
          .groupBy(bucketMapper)
          .map{ case (bucketName, occurrences) => (occurrences.max, bucketName, occurrences.size)}
          .toList
          .sortBy{ case (value, bucketName, count) => value}
          .map{ case (value, bucketName, count) => (bucketName, count)}
    }

}
