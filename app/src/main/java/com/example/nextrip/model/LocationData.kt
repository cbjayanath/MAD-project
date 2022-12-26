package com.example.nextrip.model

data class LocationData(
    val locationid: String ?= null,
    val name: String ?= null,
    val city: String ?= null,
    val district: String ?= null,
    val description: String ?= null,
    val arrivaldate: String ?= null,
    val arrivaltime: String ?= null,
    val addedDate: String ?= null,
    val addedTime: String?= null,
    val tripid: String ?= null
)
