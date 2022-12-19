package com.example.nextrip.model

import android.telephony.emergency.EmergencyNumber
import com.example.nextrip.Member

data class MemberData (
    var memberName : String ?= null,
    var memberMobile : String ?= null,
    var memberEmergencyNumber: String ?= null,
    var memberAddress: String ?= null,
    var tripid: String ?= null,
    var expandable :Boolean=false

)