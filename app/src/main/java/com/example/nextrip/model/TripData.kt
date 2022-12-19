package com.example.nextrip.model

data class TripData(
    var tripid: String ?= null,
    var tripname: String ?= null,
    var tripdescription: String ?= null,
    var startDate: String ?= null,
    var endDate: String ?= null,
    var userid: String ?= null,
    var isEnd: Boolean ?= null)
