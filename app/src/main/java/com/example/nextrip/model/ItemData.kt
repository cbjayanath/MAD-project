package com.example.nextrip.model

data class ItemData(
    val itemid: String ?= null,
    val name: String ?= null,
    val quantity: String? = null,
    val description: String ?= null,
    val rented: String ?= null,
    val tripid: String ?= null
){}