package edu.agh.roomie.rest.model

import kotlin.random.Random

fun countScore(first: User, second: User): Int {
    //TODO: implement scoring algorithm
    return Random.nextInt(0, 101)
}