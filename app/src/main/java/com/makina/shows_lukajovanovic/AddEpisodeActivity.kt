package com.makina.shows_lukajovanovic

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


class AddEpisodeActivity : AppCompatActivity(), SeasonEpisodePickerDialog.NoticeDialogListener, TakePhotoDialog.TakePhotoDialogListener {
	companion object {
		const val EPISODE_CODE = "EPISODE_CODE"
		const val PHOTO_URI_CODE = "PHOTO_URI_CODE"
		private const val REQUEST_GALLERY = 2
		private const val REQUEST_CAMERA = 1
		private const val REQUEST_CODE_PERMISSION_GALLERY = 1
		private const val REQUEST_CODE_PERMISSIONS_CAMERA = 2
		private const val REQUEST_CODE_PERMISSION_SETPHOTO = 3

		fun newInstance(context: Context) : Intent {
			return Intent(context, AddEpisodeActivity::class.java)
		}
	}

	private var photoUri: Uri? = null
	var bitmapEpisode: Bitmap? = null
	private var curEpisode:Episode = Episode()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_episode)
		setSupportActionBar(toolbarAddEpisode)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.title = "Add episode"

		curEpisode = (savedInstanceState?.getSerializable(EPISODE_CODE) as? Episode) ?: Episode()

		loadTextViewSE()

		buttonSave.setOnClickListener {
			val resultIntent = Intent()
			resultIntent.putExtra(EPISODE_CODE, curEpisode)
			setResult(RESULT_OK, resultIntent)
			finish()
		}
		linearLayoutSeasonEpisodePicker.setOnClickListener {
			SeasonEpisodePickerDialog(curEpisode.seasonNum, curEpisode.episodeNum).show(supportFragmentManager, "timePicker")
		}

		editTextEpisodeName.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.name = editTextEpisodeName.text.toString()
			}
		})
		editTextEpisodeDescription.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.episodeDescription = editTextEpisodeDescription.text.toString()
			}
		})

		imageButtonTakePhoto.setOnClickListener {
			TakePhotoDialog().show(supportFragmentManager, "takePhotoDialog")
		}
	}

	public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
		if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			photoUri = data?.data
		}
		if((requestCode == REQUEST_GALLERY || requestCode == REQUEST_CAMERA) && resultCode == RESULT_OK) {
			setPhoto()
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putSerializable(EPISODE_CODE, curEpisode)
		outState.putParcelable(PHOTO_URI_CODE, photoUri)
		super.onSaveInstanceState(outState)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		curEpisode = savedInstanceState.getSerializable(EPISODE_CODE) as? Episode ?: Episode()
		editTextEpisodeName.setText(curEpisode.name)
		editTextEpisodeDescription.setText(curEpisode.episodeDescription)
		loadTextViewSE()
		photoUri = savedInstanceState.getParcelable(PHOTO_URI_CODE)
		setPhoto()
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if(item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onDialogSaveButton(dialog: SeasonEpisodePickerDialog) {
		curEpisode.episodeNum = dialog.curView.numberPickerEpisode.value
		curEpisode.seasonNum = dialog.curView.numberPickerSeason.value
		loadTextViewSE()
	}

	override fun onDialogSelect(which: String) {
		if(which == TakePhotoDialog.CAMERA_STRING) {
			//Camera
			takePhotoFromCamera()
		} else {
			//Gallery
			choosePhotoFromGallery()
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if(grantResults.first() != PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(this, "Please provide storage permission.", Toast.LENGTH_SHORT).show()
			return
		}
		when (requestCode) {
			REQUEST_CODE_PERMISSION_GALLERY -> choosePhotoFromGallery()
			REQUEST_CODE_PERMISSIONS_CAMERA -> takePhotoFromCamera()
			REQUEST_CODE_PERMISSION_SETPHOTO -> setPhoto()
		}
	}

	private fun handlePermission(curPermission: String, requestCode: Int):Boolean {
		if(ActivityCompat.checkSelfPermission(this, curPermission) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, arrayOf(curPermission), requestCode)
			return false
		}
		return true
	}

	private fun choosePhotoFromGallery() {
		if(!handlePermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION_GALLERY)) return
		//Imam permission
		val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(galleryIntent, REQUEST_GALLERY)
	}

	private fun takePhotoFromCamera() {
		if(!handlePermission(Manifest.permission.CAMERA, REQUEST_CODE_PERMISSIONS_CAMERA)) return
		if(!handlePermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSIONS_CAMERA)) return
		if(!handlePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSIONS_CAMERA)) return
		//Imam permissione

		val photoFile: File? = try {
			createImageFile()
		} catch (ex: IOException) {
			null
		}
		val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		takePhotoIntent.resolveActivity(packageManager)?.also {
			photoFile?.also {
				photoUri = FileProvider.getUriForFile(this, "com.makina.shows_lukajovanovic", it)
				takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
				startActivityForResult(takePhotoIntent, REQUEST_CAMERA)
			}
		}
	}

	private fun setPhoto() {
		if(!handlePermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION_SETPHOTO)) return
		//Imam permission

		photoUri?.apply {
			bitmapEpisode = getBitmap(this)
		}
		if(bitmapEpisode == null) {
			imageButtonTakePhoto.setImageResource(R.drawable.ic_camera)
		}
		else {
			imageButtonTakePhoto.setImageBitmap(bitmapEpisode)
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
		//TODO try catch?
		val input: InputStream? = this.contentResolver.openInputStream(uri)
		val options: BitmapFactory.Options = BitmapFactory.Options()
		//TODO opcije
		val resultBitmap: Bitmap? = BitmapFactory.decodeStream(input, null, options)
		input?.close()
		return resultBitmap
	}


	@Throws(IOException::class)
	private fun createImageFile(): File? {
		// Create an image file name
		///val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
		val timeStamp = Calendar.getInstance().timeInMillis
		val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
		val resultFile: File = File.createTempFile(
				"JPEG_${timeStamp}_",
				".jpg",
				storageDir)

		return resultFile
	}
}
