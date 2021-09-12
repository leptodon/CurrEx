package ru.cactus.currex

fun Float.formatToShortString(digits: Int) = "%.${digits}f".format(this)