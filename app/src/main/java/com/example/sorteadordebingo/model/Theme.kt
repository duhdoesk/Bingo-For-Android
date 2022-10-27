package com.example.sorteadordebingo.model

import kotlinx.serialization.Serializable

@Serializable
data class Theme(
    val id: String,
    val name: String,
    val elements: List<Element>
)