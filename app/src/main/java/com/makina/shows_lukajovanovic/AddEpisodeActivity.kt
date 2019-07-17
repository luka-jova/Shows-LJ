package com.makina.shows_lukajovanovic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.TextureView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.*
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.view.*
import android.widget.Toast
import android.webkit.PermissionRequest
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class AddEpisodeActivity : AppCompatActivity(), SeasonEpisodePickerDialog.NoticeDialogListener, TakePhotoDialog.TakePhotoDialogListener {
	override fun onDialogSelect(which: String) {
		Log.d("moj tag", which)
		if(which == TakePhotoDialog.CAMERA_STRING) {
			//Camera
			/**dispatchTakePictureIntent()*/
			///takePhotoFromCamera()
			dispatchTakePictureIntent()
		} else {
			//Gallery
			choosePhotoFromGallery()
		}
	}
/**
	val REQUEST_IMAGE_CAPTURE = 1

	private fun dispatchTakePictureIntent() {
		Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
			takePictureIntent.resolveActivity(packageManager)?.also {
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
			}
		}
	}
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			val imageBitmap = data?.extras?.get("data") as Bitmap
			imageButtonTakePhoto.setImageBitmap(imageBitmap)
		}
	}*/

	var bitmapEpisode: Bitmap? = null

	fun setPhoto() {
		if(bitmapEpisode == null) {
			imageButtonTakePhoto.setImageResource(R.drawable.ic_show_password)
		}
		else {
			imageButtonTakePhoto.setImageBitmap(bitmapEpisode)
		}
	}


	public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
		Toast.makeText(this, "onActivityResult $requestCode $resultCode", Toast.LENGTH_SHORT).show()
		Log.d("moj tag", "onActivityResult $requestCode $resultCode")
		if(requestCode == GALLERY && resultCode == RESULT_OK) {

			if (data != null) {
				Log.d("moj tag", "dimenzije: ")
				val contentURI = data!!.data
				try {
					bitmapEpisode = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
					imageButtonTakePhoto.setImageBitmap(bitmapEpisode)
				}
				catch (e: IOException) {
					e.printStackTrace()
				}
			}
		}
		/*else if (requestCode == CAMERA && resultCode == RESULT_OK) {
			bitmapEpisode = data!!.extras!!.get("data") as Bitmap
			imageButtonTakePhoto.setImageBitmap(bitmapEpisode)
			saveImage(bitmapEpisode!!)
		}*/
		else if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			bitmapEpisode = MediaStore.Images.Media.getBitmap(this.contentResolver,
				data!!.extras!!.get(MediaStore.EXTRA_OUTPUT) as Uri?
			)
			setPhoto()
		}

	}


	companion object {
		const val EPISODE_CODE = "EPISODE_CODE"
		const val BITMAP_CODE = "BITMAP_CODE"
		private val IMAGE_DIRECTORY = "/demonuts"
		fun newInstance(context: Context) : Intent {
			val intent = Intent(context, AddEpisodeActivity::class.java)
			return intent
		}
	}

	private var curEpisode:Episode = Episode()

	override fun onSaveInstanceState(outState: Bundle) {
		Log.d("moj tag", "sejvam")
		outState.putParcelable(BITMAP_CODE, bitmapEpisode)
		outState.putSerializable(EPISODE_CODE, curEpisode)
		super.onSaveInstanceState(outState)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		Log.d("moj tag", "loadam")
		///OVO JE TEORETSKI VISKA, ALI OSTAVLJAM ZATO STO AKO ZELIM U TEKST POSTAVITI NESTO SVOJE (NE SEJVAN TEKST) ONDA AKO POZIVAM SAMO U onCreate-u TO POSTAVLJANJE TEKSTA, super(...) u ovoja metodi CE UBACITI ZAPRAVO SEJVANI TEKST TE PREBRISATI ONO STO SAM STAVIO U onCreate-u
		editTextEpisodeName.setText(curEpisode.name)
		editTextEpisodeDescription.setText(curEpisode.episodeDescription)

		bitmapEpisode = savedInstanceState?.getParcelable(BITMAP_CODE)
		setPhoto()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_episode)
		setSupportActionBar(toolbarAddEpisode)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.title = "Add episode"

		requestMultiplePermissions()
		curEpisode = (savedInstanceState?.getSerializable(EPISODE_CODE) as? Episode) ?: Episode()

		//**LOAD STATE*//
		editTextEpisodeName.setText(curEpisode.name)
		editTextEpisodeDescription.setText(curEpisode.episodeDescription)
		loadTextViewSE()


		buttonSave.setOnClickListener {
			val resultIntent = Intent()
			resultIntent.putExtra(EPISODE_CODE, curEpisode)
			setResult(RESULT_OK, resultIntent)
			finish()
		}
		linearLayoutSeasonEpisodePicker.setOnClickListener {
			Log.d("moj tag", "otvaram Fragment ${curEpisode.seasonNum} ${curEpisode.episodeNum}")
			SeasonEpisodePickerDialog(curEpisode.seasonNum, curEpisode.episodeNum).show(supportFragmentManager, "timePicker")
		}

		editTextEpisodeName.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.name = this@AddEpisodeActivity.editTextEpisodeName.text.toString()
			}
		})
		editTextEpisodeDescription.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.episodeDescription = this@AddEpisodeActivity.editTextEpisodeDescription.text.toString()
			}
		})

		imageButtonTakePhoto.setOnClickListener {
			TakePhotoDialog().show(supportFragmentManager, "takePhotoDialog")
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if(item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onDialogSaveButton(dialog: SeasonEpisodePickerDialog) {
		//TODO dialog.view uopce nije dobro postavljen: if(dialog.view?.editTextNapisiNesto == null) {Log.d("moj tag", "editTextNapisiNesto je null")}
		curEpisode.episodeNum = dialog.curView.numberPickerEpisode.value
		curEpisode.seasonNum = dialog.curView.numberPickerSeason.value
		loadTextViewSE()
	}

	fun loadTextViewSE() {
		var cE:String = "--"
		if(curEpisode.episodeNum != -1) cE = curEpisode.episodeNum.toString()
		var cS:String = "--"
		if(curEpisode.seasonNum != -1) cS = curEpisode.seasonNum.toString()
		textViewSeasonEpisode.text = "S ${cS.padStart(2, '0')}, E ${cE.padStart(2, '0')}"
	}

	lateinit var currentPhotoPath: String

	@Throws(IOException::class)
	private fun createImageFile(): File {
		// Create an image file name
		val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
		val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
		return File.createTempFile(
			"JPEG_${timeStamp}_", /* prefix */
			".jpg", /* suffix */
			storageDir /* directory */
		).apply {
			// Save a file: path for use with ACTION_VIEW intents
			currentPhotoPath = absolutePath
		}
	}
	val REQUEST_TAKE_PHOTO = 3

	private fun dispatchTakePictureIntent() {
		Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
			// Ensure that there's a camera activity to handle the intent
			takePictureIntent.resolveActivity(packageManager)?.also {
				// Create the File where the photo should go
				val photoFile: File? = try {
					createImageFile()
				} catch (ex: IOException) {
					// Error occurred while creating the File
					null
				}
				// Continue only if the File was successfully created
				photoFile?.also {
					val photoURI: Uri = FileProvider.getUriForFile(
						this,
						"com.example.android.fileprovider",
						it
					)
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
					startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
				}
			}
		}
	}



	////*****///
	private fun requestMultiplePermissions() {
		Dexter.withActivity(this)
			.withPermissions(
				Manifest.permission.CAMERA,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE
			)
			.withListener(object : MultiplePermissionsListener {
				override fun onPermissionRationaleShouldBeShown(
					permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
					token: PermissionToken?
				) {
					token?.continuePermissionRequest()
				}

				override fun onPermissionsChecked(report: MultiplePermissionsReport) {
					// check if all permissions are granted
					if (report.areAllPermissionsGranted()) {
						Toast.makeText(applicationContext, "All permissions are granted by user!", Toast.LENGTH_SHORT).show()
					}

					// check for permanent denial of any permission
					if (report.isAnyPermissionPermanentlyDenied()) {
						// show alert dialog navigating to Settings
						//openSettingsDialog();
					}
				}

			}).withErrorListener(object : PermissionRequestErrorListener {
				override fun onError(error: DexterError) {
					Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show()
				}
			})
			.onSameThread()
			.check()
	}
	private val GALLERY = 1
	private val CAMERA = 2

	fun choosePhotoFromGallery() {
		val galleryIntent = Intent(Intent.ACTION_PICK,
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

		startActivityForResult(galleryIntent, GALLERY)
	}

	private fun takePhotoFromCamera() {
		val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		startActivityForResult(intent, CAMERA)
	}


	fun saveImage(myBitmap: Bitmap):String {
		val bytes = ByteArrayOutputStream()
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
		val wallpaperDirectory = File(
			(Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
		// have the object build the directory structure, if needed.
		Log.d("fee",wallpaperDirectory.toString())
		if (!wallpaperDirectory.exists())
		{

			wallpaperDirectory.mkdirs()
		}

		try
		{
			Log.d("heel",wallpaperDirectory.toString())
			val f = File(wallpaperDirectory, ((Calendar.getInstance()
				.getTimeInMillis()).toString() + ".jpg"))
			f.createNewFile()
			val fo = FileOutputStream(f)
			fo.write(bytes.toByteArray())
			MediaScannerConnection.scanFile(this,
				arrayOf(f.getPath()),
				arrayOf("image/jpeg"), null)
			fo.close()
			Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

			return f.getAbsolutePath()
		}
		catch (e1: IOException) {
			e1.printStackTrace()
		}

		return ""
	}

}
