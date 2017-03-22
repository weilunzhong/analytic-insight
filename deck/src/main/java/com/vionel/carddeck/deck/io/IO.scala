package com.vionel.carddeck.deck.io

import java.io.{BufferedWriter, File, FileWriter}

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{JsonSerializer, ObjectMapper, SerializerProvider}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.vionel.carddeck.deck.datainterface._
import com.vionel.carddeck.deck.evaluation.{ContentType, InteractionType, UCI, UserType}

import scala.collection.mutable
import scala.io.Source
import scala.reflect.ClassTag

object IO {

    def readUCIs[A <: UserType: ClassTag, B <: ContentType: ClassTag, C <: InteractionType: ClassTag]
    (interactionFile: File, userFile: File, contentFile: File): List[UCI[A, B, C]] = {

        val mapper = objectMapper()
        val users = new mutable.HashMap[String, A]()
        val contents = new mutable.HashMap[String, B]()

        Source.fromFile(userFile).getLines().foreach{ userJSON =>
            val user = mapper.readValue(userJSON, implicitly[ClassTag[A]].runtimeClass).asInstanceOf[A]
            users(user.id) = user
        }

        Source.fromFile(contentFile).getLines().foreach{ contentJSON =>
            val content = mapper.readValue(contentJSON,  implicitly[ClassTag[B]].runtimeClass).asInstanceOf[B]
            contents(content.id) = content
        }

        Source.fromFile(interactionFile).getLines().map{ interactionJSON =>
            val i = mapper.readValue(interactionJSON, implicitly[ClassTag[C]].runtimeClass).asInstanceOf[C]
            new UCI[A, B, C]{
                val user = users(i.userID)
                val content = contents(i.contentID)
                val interaction = i
            }
        }.toList
    }

    def writeCards(outputFile: File, cards: List[Card]) = {
        val mapper = objectMapper()
        val bw = new BufferedWriter(new FileWriter(outputFile))
        bw.write(mapper.writeValueAsString(cards))
        bw.close()
    }

    def objectMapper() = {

        val mapper = new ObjectMapper
        mapper.registerModule(new DefaultScalaModule)

        val objectModule: SimpleModule = new SimpleModule("CustomSerializers")
        objectModule.addSerializer(classOf[Datetime], datetimeSerializer)
        objectModule.addSerializer(classOf[Scale], scaleSerializer)
        mapper.registerModule(objectModule)
        mapper

    }

    private val datetimeSerializer = new JsonSerializer[Datetime]() {
        def serialize(datetime: Datetime, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
            jsonGenerator.writeNumber(datetime.epochSeconds)
        }
    }
    private val scaleSerializer = new JsonSerializer[Scale]() {
        def serialize(scale: Scale, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
            jsonGenerator.writeString(scale.`type`)
        }
    }

}