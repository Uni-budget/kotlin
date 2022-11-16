package com.example.babygage_ocr

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.babygage_ocr.databinding.ActivityImportimageBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

@Suppress("DEPRECATION")
class ImportimageActivity : AppCompatActivity(), View.OnClickListener {
    val CAMERA = 100 // camera intent value
    val GALLERY = 101 // gallery intent value
    var imagePath = ""

    @SuppressLint("SimpleDateFormat")
    var imageDate: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")

    var btnCamera: Button? = null
    var btnGallery: Button? = null
    var btnMove: Button? = null
    var imageView: ImageView? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityImportimageBinding
        binding = ActivityImportimageBinding.inflate(layoutInflater)
        setContentView(binding.root)




        imageView =binding.ivMain
        btnCamera = binding.btnCamera
        btnGallery = binding.btnGallery
        btnMove = binding.btnMove
        btnCamera!!.setOnClickListener(this)
        btnGallery!!.setOnClickListener(this)
        btnMove!!.setOnClickListener(this)



        val hasCamPerm =
            checkSelfPermission(android.Manifest.permission.CAMERA) === PackageManager.PERMISSION_GRANTED
        val hasWritePerm =
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED
        if (!hasCamPerm || !hasWritePerm) // for user permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )


    }


    @SuppressLint(*["NonConstantResourceId", "QueryPermissionsNeeded"])
    override fun onClick(view: View) {
        when (view.getId()) {
            R.id.btn_camera -> {
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent!!.resolveActivity(getPackageManager()) != null) {
                    var imageFile: File? = null
                    try {
                        imageFile = createImageFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (imageFile != null) {
                        val imageUri: Uri = FileProvider.getUriForFile(
                            getApplicationContext(),
                            "com.example.sendimage.fileprovider",
                            imageFile
                        )
                        intent!!.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(intent, CAMERA)
                    }
                }
            }
            R.id.btn_gallery -> {
                intent = Intent(Intent.ACTION_PICK)
                intent!!.setType(MediaStore.Images.Media.CONTENT_TYPE)
                intent!!.setType("image/*")
                startActivityForResult(intent, GALLERY)
            }
            R.id.btn_move -> if (imagePath.length > 0) { // if there exists image path
                intent = Intent(getApplicationContext(), SetImageActivity::class.java)
                intent!!.putExtra("path", imagePath)
                startActivity(intent)
            }
        }
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) { // if there exists result
            if (requestCode == GALLERY) { // if choose gallery
//				1) data address
                imagePath =
                    data?.getDataString().toString() // "content://media/external/images/media/7215"
                //				2) absolute path
                val cursor =
                    data?.getData()?.let { getContentResolver().query(it, null, null, null, null) }
                if (cursor != null) {
                    cursor.moveToFirst()
                    val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    imagePath = cursor.getString(index) // "/media/external/images/media/7215"
                    cursor.close()
                }
            }
            if (imagePath.length > 0) {
                imageView?.let {
                    Glide.with(this)
                        .load(imagePath)
                        .into(it)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
//        create image file
        val timeStamp: String =
            imageDate.format(Date()) // to avoid duplicate, we used timestamp in form of "yyyyMMdd_HHmmss"
        val fileName = "IMAGE_$timeStamp" // image file name
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file: File = File.createTempFile(fileName, ".jpg", storageDir) // create image file
        imagePath = file.getAbsolutePath() // save absolute path
        return file
    }


}