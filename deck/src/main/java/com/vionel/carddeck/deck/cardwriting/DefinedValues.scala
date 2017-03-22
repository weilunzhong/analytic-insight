package com.vionel.carddeck.deck.cardwriting

import com.vionel.carddeck.deck.datainterface.{Category, Concept, Scale}

// Categories /////////////////////////

object ReportCategory extends Category {
    val name = "REPORT"
}

// Scales /////////////////////////

object LinearScale extends Scale {
    val `type` = "LINEAR"
}

// Concepts /////////////////////////

object UserConcept extends Concept {
    val name = "USER"
}
object ContentConcept extends Concept {
    val name = "CONTENT"
}