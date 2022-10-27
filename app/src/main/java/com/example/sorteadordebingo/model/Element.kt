package com.example.sorteadordebingo.model

import kotlinx.serialization.Serializable

@Serializable
data class Element(
    val id: String,
    val name: String,
    val image: String
)