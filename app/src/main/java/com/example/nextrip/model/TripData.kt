package com.example.nextrip.model

data class TripData(
    var tripid: String ?= null,
    var tripname: String ?= null,
    var tripdescription: String ?= null,
    var startdate: String ?= null,
    var enddate: String ?= null,
    var endtime: String ?= null,
    var end: String ?= null,
    var userid: String ?= null
)
