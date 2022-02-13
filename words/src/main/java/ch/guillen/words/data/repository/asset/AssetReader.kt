package ch.guillen.words.data.repository.asset

import android.content.Context

fun Context.readFromAssets(resource: String): String =
    assets.open(resource).reader().use { it.readText() }
