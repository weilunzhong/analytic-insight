package com.vionel.carddeck.deck

import java.io.File

import com.vionel.carddeck.deck.evaluation.Execute


object DeveloperInvoker {

    def main(args: Array[String]): Unit = {
        Execute.generateCards(
            interactionFile = new File("/home/erik/data/insight/uci_interactions.json"),
            userFile = new File("/home/erik/data/insight/uci_users.json"),
            contentFile = new File("/home/erik/data/insight/uci_contents.json"),
            cardsOutputFile = new File("/home/erik/Git/frontendstories/app/stories.json")
        )
    }

}
