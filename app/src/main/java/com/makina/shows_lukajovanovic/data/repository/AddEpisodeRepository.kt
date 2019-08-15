package com.makina.shows_lukajovanovic.data.repository

import android.net.Uri
import android.os.Environment
import android.os.FileUtils
import android.util.Log
import androidx.core.graphics.PathUtils
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.EpisodePostData
import com.makina.shows_lukajovanovic.data.model.EpisodePostResponse
import com.makina.shows_lukajovanovic.data.model.MediaResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeFragment
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream
import java.net.URI
import java.util.*


object AddEpisodeRepository {
	private val episodePostResponseMutableLiveData = MutableLiveData<EpisodePostResponse>()
	val episodePostResponseLiveData: LiveData<EpisodePostResponse>
		get() = episodePostResponseMutableLiveData

	private var mediaResponse: MediaResponse? = null
	private var callUploadPhoto: Call<MediaResponse>? = null
	private var callUploadEpisode: Call<EpisodePostResponse>? = null

	fun addEpisode(showId: String, episode: Episode, photoUri: Uri?) {
		if(episodePostResponseMutableLiveData.value?.status == ResponseStatus.DOWNLOADING) return
		episodePostResponseMutableLiveData.value = EpisodePostResponse(status = ResponseStatus.DOWNLOADING)

		/**Upload photo*/
		if(photoUri == null) {
			uploadEpisode(showId, episode)
			return
		}
		val file: File? = getFileFromUri(photoUri)
		if(file == null) {listener?.displayMessage("Error", "Failed reading file"); return}
		val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
		callUploadPhoto = RetrofitClient.apiService?.uploadMedia(
			AuthorizationRepository.tokenResponseLiveData.value?.token ?: "",
			requestFile
		)
		callUploadPhoto?.enqueue(object: Callback<MediaResponse> {
			override fun onFailure(call: Call<MediaResponse>, t: Throwable) {
				episodePostResponseMutableLiveData.value = EpisodePostResponse(status = ResponseStatus.FAIL)
				if(call.isCanceled) return
				listener?.displayMessage("Error", "Connection error")
			}

			override fun onResponse(call: Call<MediaResponse>, response: Response<MediaResponse>) {
				if(!response.isSuccessful || response.body() == null) {
					episodePostResponseMutableLiveData.value = EpisodePostResponse(status = ResponseStatus.FAIL)
					listener?.displayMessage("Error", "Failed to upload image")
					return
				}
				mediaResponse = response.body()
				uploadEpisode(showId, episode)
			}
		})

	}

	var listener: RepositoryInfoHandler? = null
	var listenerFragment: AddEpisodeFragmentListener? = null
	interface AddEpisodeFragmentListener {
		fun onSuccessUpload()
	}

	private fun getFileFromUri(photoUri: Uri): File? {
		val input = ShowsApp.instance.contentResolver.openInputStream(photoUri)
		val timeStamp = Calendar.getInstance().timeInMillis
		val storageDir: File = ShowsApp.instance.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
		val resultFile: File = File.createTempFile(
			"JPEG_${timeStamp}_",
			".jpg",
			storageDir)

		val output = FileOutputStream(resultFile)
		val buf = ByteArray(1024)
		var len: Int
		len = input?.read(buf) ?: 0

		while (len > 0) {
			output.write(buf, 0, len)
			len = input?.read(buf) ?: 0
		}

		output.close()
		input?.close()
		return resultFile
	}

	private fun uploadEpisode(showId: String, episode: Episode) {
		val photoId: String = mediaResponse?.mediaResponseData?.imageId ?: ""
		callUploadEpisode =
			RetrofitClient.apiService?.uploadEpisode(
			AuthorizationRepository.tokenResponseLiveData.value?.token ?: "",
				EpisodePostData(showId, photoId, episode.name, episode.episodeDescription, episode.episodeNumString, episode.seasonNumString)
			)

		callUploadEpisode?.enqueue(object: Callback <EpisodePostResponse> {
			override fun onFailure(call: Call<EpisodePostResponse>, t: Throwable) {
				episodePostResponseMutableLiveData.value = EpisodePostResponse(status = ResponseStatus.FAIL)
				if(call.isCanceled) return
				listener?.displayMessage("Error", "Connection error")
			}

			override fun onResponse(call: Call<EpisodePostResponse>, response: Response<EpisodePostResponse>) {
				if(!response.isSuccessful || response.body() == null) {
					episodePostResponseMutableLiveData.value = EpisodePostResponse(status = ResponseStatus.FAIL)
					listener?.displayMessage("Error", "Failed uploading")
					return
				}
				episodePostResponseMutableLiveData.value =
					EpisodePostResponse(
						episode = response.body()?.episode ?: Episode(),
						status = ResponseStatus.SUCCESS
					)
				listenerFragment?.onSuccessUpload()
			}
		})
	}

	fun cancelCalls() {
		callUploadPhoto?.cancel()
		callUploadEpisode?.cancel()
	}

}