package com.example.sorteadordebingo.model

import kotlinx.serialization.Serializable

@Serializable
class Theme(
    val id: String,
    val name: String,
    val cards: List<Card>
) {
}