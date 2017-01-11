package com.github.pjozsef.rumorfx.util

fun random(range: ClosedRange<Double>): Double{
    val width = range.endInclusive - range.start
    return Math.random() * width + range.start
}

fun random(number: Double) = random(0.0..number)

fun random(number: Int) = random(0..number)

fun random(range: IntRange): Int{
    return Math.round(random(range.start.toDouble()..range.endInclusive.toDouble())).toInt()
}

fun <T> List<T>.random() = get(random(size-1))

fun <T> List<T>.safeRandom(): T? = if(size > 0) get(random(size-1)) else null