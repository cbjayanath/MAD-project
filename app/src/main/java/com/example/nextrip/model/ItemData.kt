package com.example.nextrip.model

data class ItemData(
    val itemid: String ?= null,
    val name: String ?= null,
    val quantity: Int ?= 0,
    val description: String ?= null,
    val isRented: Boolean ?= false,
    val tripid: String ?= null
){}