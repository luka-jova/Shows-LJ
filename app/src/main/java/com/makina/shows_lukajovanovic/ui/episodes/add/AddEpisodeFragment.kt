package com.makina.shows_lukajovanovic.ui.episodes.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.repository.AddEpisodeRepository
import kotlinx.android.synthetic.main.fragment_add_episode.*
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.view.*
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class AddEpisodeFragment: Fragment(), AddEpisodeRepository.AddEpisodeFragmentListener {
	companion object {
		const val ADD_EPISODE_TAG = "ADD_EPISODE_TAG"
		const val EPISODE_CODE = "EPISODE_CODE"
		const val PHOTO_URI_CODE = "PHOTO_URI_CODE"
		const val SHOW_ID_CODE = "SHOW_ID_CODE"
		private const val REQUEST_GALLERY = 2
		private const val REQUEST_CAMERA = 1
		private const val REQUEST_CODE_PERMISSION_GALLERY = 1
		private const val REQUEST_CODE_PERMISSIONS_CAMERA = 2
		private const val REQUEST_CODE_PERMISSION_SETPHOTO = 3

		private const val PHOTO_STATE_ORIGINAL = 0
		private const val PHOTO_STATE_LOADED = 1

		fun newInstance(showId: String): AddEpisodeFragment {
			return AddEpisodeFragment().apply {
				arguments = Bundle().apply {
					putString(SHOW_ID_CODE, showId)
				}
			}
		}
	}

	override fun onSuccessUpload() {
		activity?.onBackPressed()
	}

	private var photoUri: Uri? = null
	private var bitmapEpisode: Bitmap? = null
	private var curEpisode: Episode = Episode()
	private var showId = ""
	private lateinit var viewModel: AddEpisodeViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_add_episode, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		toolbarAddEpisode.title = "Add episode"
		toolbarAddEpisode.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarAddEpisode.setNavigationOnClickListener { activity?.onBackPressed() }

		showId = arguments?.getString(SHOW_ID_CODE, "") ?: ""
		viewModel = ViewModelProviders.of(this).get(AddEpisodeViewModel::class.java)
		viewModel.addListener(this)
		curEpisode = (savedInstanceState?.getSerializable(EPISODE_CODE) as? Episode) ?: Episode()

		editTextEpisodeName.setText(curEpisode.name)
		editTextEpisodeDescription.setText(curEpisode.episodeDescription)
		loadTextViewSE()
		photoUri = savedInstanceState?.getParcelable(PHOTO_URI_CODE)
		setPhoto()

		buttonSave.setOnClickListener {
			viewModel.addEpisode(showId, curEpisode, photoUri)
		}
		linearLayoutSE.setOnClickListener {
			SeasonEpisodePickerDialog(
				curEpisode.seasonNum,
				curEpisode.episodeNum
			).show(requireFragmentManager(), "timePicker")
		}
		editTextEpisodeName.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.name = p0.toString()
				updateUi()
			}
		})
		editTextEpisodeDescription.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.episodeDescription = p0.toString()
				updateUi()
			}
		})

		linearLayoutPhoto.setOnClickListener {
			TakePhotoDialog().show(requireFragmentManager(), "takePhotoDialog")
		}

		viewModel.episodePostResponseLiveData.observe(this, androidx.lifecycle.Observer{response ->
			updateUi()
		})
		updateUi()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putSerializable(EPISODE_CODE, curEpisode)
		outState.putParcelable(PHOTO_URI_CODE, photoUri)
		super.onSaveInstanceState(outState)
	}

	override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
		if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
			photoUri = data?.data
		}
		if((requestCode == REQUEST_GALLERY || requestCode == REQUEST_CAMERA) && resultCode == Activity.RESULT_OK) {
			setPhoto()
		}
	}

	fun onDialogSaveButton(dialog: SeasonEpisodePickerDialog) {
		curEpisode.episodeNum = dialog.curView.numberPickerEpisode.value
		curEpisode.seasonNum = dialog.curView.numberPickerSeason.value
		loadTextViewSE()
	}

	fun onDialogSelect(which: String) {
		if(which == TakePhotoDialog.CAMERA_STRING) {
			//Camera
			takePhotoFromCamera()
		} else {
			//Gallery
			choosePhotoFromGallery()
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		for(cur in grantResults) {
			if(cur != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(requireContext(), "Please provide permission.", Toast.LENGTH_SHORT).show()
				return
			}
		}
		when (requestCode) {
			REQUEST_CODE_PERMISSION_GALLERY -> choosePhotoFromGallery()
			REQUEST_CODE_PERMISSIONS_CAMERA -> takePhotoFromCamera()
			REQUEST_CODE_PERMISSION_SETPHOTO -> setPhoto()
		}
	}

	private fun handlePermission(curPermissions: Array<String>, requestCode: Int):Boolean {
		val reqPermissions: ArrayList<String> = arrayListOf<String>()
		for(cur in curPermissions) {
			if(checkSelfPermission(requireContext(), cur) != PackageManager.PERMISSION_GRANTED) {
				reqPermissions.add(cur)
			}
		}
		if(reqPermissions.isEmpty()) return true
		requestPermissions(reqPermissions.toTypedArray(), requestCode)
		return false
	}

	private fun choosePhotoFromGallery() {
		if(!handlePermission(
				arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
				REQUEST_CODE_PERMISSION_GALLERY
			)) return
		//Imam permission
		val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(galleryIntent,
			REQUEST_GALLERY
		)
	}

	private fun takePhotoFromCamera() {
		if(!handlePermission(
				arrayOf(
					Manifest.permission.CAMERA,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE
				),
				REQUEST_CODE_PERMISSIONS_CAMERA
			)) return
		//Imam permissione

		val tmpFile = try {
			createImageFile()
		} catch (ex: IOException) {
			null
		}
		val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		takePhotoIntent.resolveActivity(requireContext().packageManager)?.also {
			tmpFile?.also {
				photoUri = FileProvider.getUriForFile(requireContext(), "com.makina.shows_lukajovanovic", it)
				takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
				startActivityForResult(takePhotoIntent,
					REQUEST_CAMERA
				)
			}
		}
	}

	private fun setPhoto() {
		if(photoUri == null) {
			updatePhotoVisibility(PHOTO_STATE_ORIGINAL)
			return
		}
		if(!handlePermission(
				arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
				REQUEST_CODE_PERMISSION_SETPHOTO
			)) return
		//Imam permission

		photoUri?.apply {
			bitmapEpisode = getBitmap(this)
		}
		if(bitmapEpisode == null) {
			updatePhotoVisibility(PHOTO_STATE_ORIGINAL)
		}
		else {
			imageButtonTakePhoto.setImageBitmap(bitmapEpisode)
			updatePhotoVisibility(PHOTO_STATE_LOADED)
		}
	}

	private fun updatePhotoVisibility(state: Int = PHOTO_STATE_ORIGINAL) {
		if(state == PHOTO_STATE_ORIGINAL) {
			imageButtonTakePhoto.visibility = View.GONE
			textViewUploadPhoto.visibility = View.GONE
			imageButtonTakePhotoOriginal.visibility = View.VISIBLE
			textViewUploadPhotoOriginal.visibility = View.VISIBLE
		}
		else {
			imageButtonTakePhoto.visibility = View.VISIBLE
			textViewUploadPhoto.visibility = View.VISIBLE
			imageButtonTakePhotoOriginal.visibility = View.GONE
			textViewUploadPhotoOriginal.visibility = View.GONE
		}
	}

	private fun loadTextViewSE() {
		var cE:String = "--"
		if(curEpisode.episodeNum != -1) cE = curEpisode.episodeNum.toString()
		var cS:String = "--"
		if(curEpisode.seasonNum != -1) cS = curEpisode.seasonNum.toString()
		textViewSeasonEpisode.text = "S ${cS.padStart(2, '0')}, E ${cE.padStart(2, '0')}"
	}

	private fun getBitmap(uri: Uri): Bitmap? {
		val input: InputStream? = requireContext().contentResolver.openInputStream(uri)
		val options: BitmapFactory.Options = BitmapFactory.Options()
		val resultBitmap: Bitmap? = BitmapFactory.decodeStream(input, null, options)
		input?.close()
		return resultBitmap
	}


	@Throws(IOException::class)
	private fun createImageFile(): File? {
		// Create an image file name
		///val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
		val timeStamp = Calendar.getInstance().timeInMillis
		val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
		val resultFile: File = File.createTempFile(
			"JPEG_${timeStamp}_",
			".jpg",
			storageDir)

		return resultFile
	}

	private fun updateUi() {
		val response = viewModel.episodePostResponse
		if(response?.status == ResponseStatus.DOWNLOADING) {
			progressBarDownloading.visibility = View.VISIBLE
			buttonSave.isEnabled = false
		}
		else {
			progressBarDownloading.visibility = View.GONE
			buttonSave.isEnabled = editTextEpisodeDescription.text.toString().length >= 50 && editTextEpisodeName.text.toString().isNotEmpty()
		}
	}


}