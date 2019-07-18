package com.makina.shows_lukajovanovic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.io.InputStream


class AddEpisodeActivity : AppCompatActivity(), SeasonEpisodePickerDialog.NoticeDialogListener, TakePhotoDialog.TakePhotoDialogListener {
	companion object {
		const val EPISODE_CODE = "EPISODE_CODE"
		const val BITMAP_CODE = "BITMAP_CODE"
		private const val REQUEST_GALLERY = 2
		private const val REQUEST_CAMERA = 1
		private const val REQUEST_CODE_PERMISSION_GALLERY = 1

		fun newInstance(context: Context) : Intent {
			return Intent(context, AddEpisodeActivity::class.java)
		}
	}

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

	public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
		Toast.makeText(this, "onActivityResult $requestCode $resultCode", Toast.LENGTH_SHORT).show()
		if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			val contentUri: Uri? = data?.data
			Log.d("tigar", contentUri?.path ?: "null je")

			if(contentUri == null) {/**TODO srusi program? javi gresku?*/Log.d("tigar", "contentUri je null")}
			else bitmapEpisode = getBitmap(contentUri)
		}
		else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

		}
		setPhoto()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		Log.d("moj tag", "sejvam")
		outState.putParcelable(BITMAP_CODE, bitmapEpisode)
		outState.putSerializable(EPISODE_CODE, curEpisode)
		super.onSaveInstanceState(outState)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		curEpisode = savedInstanceState.getSerializable(EPISODE_CODE) as? Episode ?: Episode()
		editTextEpisodeName.setText(curEpisode.name)
		editTextEpisodeDescription.setText(curEpisode.episodeDescription)
		loadTextViewSE()

		bitmapEpisode = savedInstanceState.getParcelable(BITMAP_CODE)
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
		Log.d("tigar", "onRequestPermissionResult")
		if(requestCode == REQUEST_CODE_PERMISSION_GALLERY) {
			if(grantResults.first() == PackageManager.PERMISSION_GRANTED) {
				choosePhotoFromGallery()
			}
			else {
				Toast.makeText(this, "Please provide storage permission.", Toast.LENGTH_SHORT).show()
				Log.d("tigar", "permission odbijen")
			}
		}

	}

	private fun choosePhotoFromGallery() {
		Log.d("tigar", "choosePhotoFromGallery")
		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			//Nemam permission
			Log.d("tigar", "nemam permission")
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION_GALLERY)
		}
		else {
			//Imam permission
			Log.d("tigar", "imam permission")
			val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
			startActivityForResult(galleryIntent, REQUEST_GALLERY)
		}
	}

	private fun takePhotoFromCamera() {
		return
		val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		startActivityForResult(intent, REQUEST_CAMERA)
	}

	private fun setPhoto() {
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

	private fun requestPermission() {

	}


}
