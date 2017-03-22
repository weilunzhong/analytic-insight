package com.vionel.carddeck.deck.datainterface

class Card(
    val name: String,
    val description: String,
    val category: Category,
    val headEntity: Entity,
    val body: Body
) {
    val id = IDGenerator.next()
}

class Body(
    val entities: List[Entity]
)

trait Category {
    val name: String
}

trait Entity {
    val `type`: String = this.getClass.getSimpleName
}

object IDGenerator {

    private var nextID = 0

    private def incrementAndGet() = {
        nextID += 1
        nextID
    }

    def next() = {
        synchronized(
            incrementAndGet()
        )
    }
}
