package com.stevdza_san.ssvapp.model

import org.bson.types.ObjectId

data class MyUser(
    val _id: ObjectId = ObjectId(),
    val ownerId: String,
    val coins: Int,
    val earnedCoins: Int
)