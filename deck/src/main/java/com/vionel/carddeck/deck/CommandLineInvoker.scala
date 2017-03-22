package com.vionel.carddeck.deck

import java.io.File

import com.vionel.carddeck.deck.evaluation.Execute
import com.vionel.carddeck.deck.io.ArgumentParser
import com.vionel.carddeck.deck.io.ArgumentParser.{InvalidArguments, ValidArguments}


object CommandLineInvoker {

    /**
      * Run 'java -jar <JAR> <args...>
      * @param args
        -input_uci_interactions <inputPathUCIInteractions>
        -input_uci_users <inputPathUCIUsers>
        -input_uci_contents <inputPathUCIContents>
        -output <outputPath>
      */
    def main(args: Array[String]): Unit = {
        ArgumentParser.parseArguments(args) match {
            case ValidArguments(
            inputPathUCIInteractions,
            inputPathUCIUsers,
            inputPathUCIContents,
            outputPath
            ) => Execute.generateCards(
                interactionFile = new File(inputPathUCIInteractions),
                userFile = new File(inputPathUCIUsers),
                contentFile = new File(inputPathUCIContents),
                cardsOutputFile = new File(outputPath)
            )
            case InvalidArguments(message) => abort(message)
        }
    }

    private def abort(message: String): Unit = {
        println(message)
        System.exit(1)
    }

}
