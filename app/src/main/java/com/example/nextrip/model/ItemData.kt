package com.example.nextrip.model

data class ItemData(
    val itemid: String ?= null,
    val name: String ?= null,
    val quantity: Int ?= 0,
    val description: String ?= null,
    val rented: String ?= null,
    val tripid: String ?= null
){}