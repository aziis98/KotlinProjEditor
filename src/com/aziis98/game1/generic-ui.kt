package com.aziis98.game1


// Copyright 2016 Antonio De Lucreziis

class UIContainer(val node: XmlNode) {

    var rectangle: Rectangle? = null

    fun updateLayout() {
        // Int(defined size) or Null(automatic sizing)
        val width = node.attributes["width"].asInt()
        val height = node.attributes["height"].asInt()


    }

    private fun String?.asInt(): Int? {
        if (this == null) return null

        return this.toInt()
    }

}

data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int) {

    companion object {
        fun betweenPoints(x1: Int, y1: Int, x2: Int, y2: Int) = Rectangle(
            Math.min(x1, x2),
            Math.min(y1, y2),
            Math.abs(x1 - x2),
            Math.abs(y1 - y2)
        )
    }

    fun contains(point2i: Point2i) = contains(point2i.x, point2i.y)

    fun contains(x: Int, y: Int) = x.inside(this.x, this.x + this.width) &&
        y.inside(this.y, this.y + this.height)

    fun intersectsWith(other: Rectangle) = contains(other.x, other.y) ||
        contains(other.x + other.width, other.y) ||
        contains(other.x, other.y + other.height) ||
        contains(other.x + width, other.y + other.height)

}

data class Point2i(val x: Int = 0, val y: Int = 0) {

    operator fun plus(other: Point2i) = Point2i(this.x + other.x, this.y + other.y)

    operator fun minus(other: Point2i) = Point2i(this.x - other.x, this.y - other.y)

    operator fun div(divisor: Int) = Point2i(this.x / divisor, this.y / divisor)

}

fun Int.inside(a: Int, b: Int) = this >= a && this <= b





















