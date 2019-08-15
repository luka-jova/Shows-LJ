package com.makina.shows_lukajovanovic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makina.shows_lukajovanovic.data.database.dao.LikeStatusDao
import com.makina.shows_lukajovanovic.data.model.LikeStatus

@Database(
	version = 4,
	entities = [
		LikeStatus::class
	]
)
abstract class LikeStatusDatabase: RoomDatabase() {
	abstract fun likeStatusDao(): LikeStatusDao
}