package com.stevdza_san.ssvapp.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.stevdza_san.ssvapp.model.MyUser
import org.bson.types.ObjectId

object MongoDB {
    private val client = MongoClient.create(System.getenv("MONGODB_URI"))
    private val database = client.getDatabase("mydb")
    private val myUserCollection = database.getCollection<MyUser>("MyUser")

    suspend fun updateCoins(studentId: String): Boolean {
        val updatedStudent = myUserCollection.findOneAndUpdate(
            filter = Filters.eq(MyUser::_id.name, ObjectId(studentId)),
            update = Updates.combine(
                Updates.inc(MyUser::coins.name, 10),
                Updates.inc(MyUser::earnedCoins.name, 10)
            )
        )
        return updatedStudent != null
    }
}