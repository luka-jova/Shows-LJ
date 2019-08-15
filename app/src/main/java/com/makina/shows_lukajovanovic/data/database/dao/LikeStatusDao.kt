package com.makina.shows_lukajovanovic.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.makina.shows_lukajovanovic.data.model.LikeStatus

@Dao
interface LikeStatusDao {
	@Query("SELECT * FROM likestatus")
	fun getAll(): LiveData<List<LikeStatus>>

	@Insert
	fun insert(likeStatus: LikeStatus): Long

	@Update
	fun update(likeStatus: LikeStatus)


}