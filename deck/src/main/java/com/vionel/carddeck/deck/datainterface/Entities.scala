package com.vionel.carddeck.deck.datainterface

import com.vionel.carddeck.deck.evaluation.Validate

// Entities ////////////////////

class BarPlot(
    val groups: List[DataGroup[String, Double]],
    val name: String,
    val subtitle: String,
    val xLabel: String,
    val yLabel: String,
    val yScale: Scale
) extends Entity

class DateLinePlot(
    val lines: List[DataGroup[Datetime, Double]],
    val name: String,
    val subtitle: String,
    val xLabel: String,
    val yLabel: String,
    val yScale: Scale
) extends Entity {
    val xScale = "TIME"
}

class NumericLinePlot(
    val lines: List[DataGroup[Double, Double]],
    val name: String,
    val subtitle: String,
    val xLabel: String,
    val yLabel: String,
    val xScale: Scale,
    val yScale: Scale
) extends Entity

class BigSmallStatement(
    val bigPart: String,
    val smallPart: String,
    val concept: Concept
) extends Entity

class PointLabelsRange(
    val pointLabels: List[PointLabel[Percentage]],
    val minValue: Double,
    val maxValue: Double
) extends Entity

// Entity elements //////////////////

trait Concept {
    val name: String
}

class XY[X, Y](
    val x: X,
    val y: Y
)

class DataGroup[X, Y](
    val name: String,
    val values: List[XY[X, Y]]
)

class PointLabel[T](
    val label: String,
    val point: T
)

trait Scale {
    val `type`: String
}

class Percentage(val percentage: Number) {
    Validate.withinRange(
        "percentage",
        value = percentage,
        min = 0,
        max = 100
    )
}

class Datetime(dateTime: org.joda.time.DateTime) {
    val epochSeconds = dateTime.getMillis / 1000
}

