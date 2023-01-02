package com.example.nextrip.model

data class EmergancyData(
    val emid: String ?= null,
    val userid: String ?= null,
    val msg: String ?= null,
    val latitude: String ?= null,
    val longitude: String ?= null,
    val locationdetails: String ?= null,
    val date: String ?= null,
    val time: String ?= null,
    val tripid: String ?= null
)
