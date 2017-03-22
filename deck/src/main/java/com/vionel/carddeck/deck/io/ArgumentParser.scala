package com.vionel.carddeck.deck.io

import org.apache.commons.cli.{DefaultParser, Option, Options, ParseException}

object ArgumentParser {

    val ARGUMENT_INPUT_INTERACTIONS_PATH = "input_uci_interactions"
    val DESCRIPTION_INPUT_INTERACTIONS_PATH = "input file path for interaction objects"

    val ARGUMENT_INPUT_USERS_PATH = "input_uci_users"
    val DESCRIPTION_INPUT_USERS_PATH = "input file path for user objects"

    val ARGUMENT_INPUT_CONTENTS_PATH = "input_uci_contents"
    val DESCRIPTION_INPUT_CONTENTS_PATH = "input file path for content objects"

    val ARGUMENT_OUTPUT_PATH = "output"
    val DESCRIPTION_OUTPUT_PATH = "output file path for card objects"

    class Arguments

    case class ValidArguments(
        inputPathUCINormalized: String,
        inputPathUCIUsers: String,
        inputPathUCIContents: String,
        outputPath: String
    ) extends Arguments

    case class InvalidArguments(message: String) extends Arguments

    def parseArguments(args: Array[String]): Arguments = {

        try {

            val commandLine = new DefaultParser().parse(options(), args)

            ValidArguments(
                commandLine.getOptionValue(ARGUMENT_INPUT_INTERACTIONS_PATH),
                commandLine.getOptionValue(ARGUMENT_INPUT_USERS_PATH),
                commandLine.getOptionValue(ARGUMENT_INPUT_CONTENTS_PATH),
                commandLine.getOptionValue(ARGUMENT_OUTPUT_PATH)
            )

        } catch {
            case e: ParseException => InvalidArguments(e.getMessage)
        }
    }

    private def options(): Options = {

        def optionWithArgument(argName: String, description: String) = {
            val o = new Option(argName, argName, true, description)
            o.setRequired(true)
            o
        }

        val options = new Options()

        options.addOption(optionWithArgument(
            argName = ARGUMENT_INPUT_INTERACTIONS_PATH,
            description = DESCRIPTION_INPUT_INTERACTIONS_PATH
        ))
        options.addOption(optionWithArgument(
            argName = ARGUMENT_INPUT_USERS_PATH,
            description = DESCRIPTION_INPUT_USERS_PATH
        ))
        options.addOption(optionWithArgument(
            argName = ARGUMENT_INPUT_CONTENTS_PATH,
            description = DESCRIPTION_INPUT_CONTENTS_PATH
        ))
        options.addOption(optionWithArgument(
            argName = ARGUMENT_OUTPUT_PATH,
            description = DESCRIPTION_OUTPUT_PATH
        ))

        options
    }

}
