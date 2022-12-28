package com.example.nextrip.model

data class TripData(
    var tripid: String ?= null,
    var tripname: String ?= null,
    var tripdescription: String ?= null,
    var startDate: String ?= null,
    var endDate: String ?= null,
    var endTime: String ?= null,
    var end: String ?= null,
    var userid: String ?= null
)
