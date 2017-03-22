package com.vionel.carddeck.deck.datainterface

import com.vionel.carddeck.deck.evaluation._
import org.joda.time.DateTime

class User(
    val id: String,
    val persona: String,
    val firstActivity: DateTime,
    val lastActivity: DateTime
) extends UserType

class Content(
    val id: String,
    val metaData: Content.MetaData
) extends ContentType

class Interaction(
    val userID: String,
    val contentID: String,
    val startTime: DateTime,
    val durationInSeconds: Double,
    val deviceType: String
) extends InteractionType {
    Validate.greaterOrEqual("durationInSeconds", durationInSeconds, 0)
}

object Content {

    class MetaData(
        val name: String,
        val durationInSeconds: Double,
        val releaseYear: Int,
        val genres: List[IdNameTuple],
        val keywords: List[IdNameTuple],
        val imdbID: String,
        val imdbRating: Int,
        val imdbNumVotes: Int
    ) {
        Validate.greaterOrEqual("durationInSeconds", durationInSeconds, 0)
        Validate.greaterOrEqual("releaseYear", releaseYear, 0)
        Validate.greaterOrEqual("imdbRating", imdbRating, 0)
        Validate.greaterOrEqual("imdbNumVotes", imdbNumVotes, 0)
    }

}

class IdNameTuple(
    val id: String,
    val name: String
)
