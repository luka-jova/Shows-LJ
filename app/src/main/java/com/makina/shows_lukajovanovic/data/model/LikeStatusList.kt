package com.makina.shows_lukajovanovic.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository
import java.io.Serializable

@Entity(tableName = "likestatus")
data class LikeStatus(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,

	@ColumnInfo(name = "show_id")
	val showId: String = "",

	@ColumnInfo(name = "like_status")
	val likeStatus: Int = EpisodesRepository.NONE_STATUS,

	@ColumnInfo(name = "status")
	val status: Int = ResponseStatus.SUCCESS
) :Serializable
