package com.vionel.carddeck.deck.evaluation


object Validate {

    def greaterOrEqual(fieldName: String, value: Double, referenceValue: Double): Unit = {
        if (value < referenceValue) {
            throw new IllegalArgumentException(s"($fieldName: $value) must be greater or equal to $referenceValue")
        }
    }

    def withinRange(fieldName: String, value: Number, min: Number, max: Number): Unit = {
        if (value.doubleValue() < min.doubleValue()) {
            throw new IllegalArgumentException(s"($fieldName: $value) must be greater or equal to $min")
        }
        if (value.doubleValue() > max.doubleValue()) {
            throw new IllegalArgumentException(s"($fieldName: $value) must be lesser or equal to $max")
        }
    }

}
