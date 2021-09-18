package com.example.pawspaceassignment.models

import com.google.firebase.Timestamp
import java.io.Serializable

data class Pet(var id: String?=null,
               var ownerID: String?= null,
               var type: String?= null,
               var weight: Int= 0,
               var timestamp: Timestamp?= null)