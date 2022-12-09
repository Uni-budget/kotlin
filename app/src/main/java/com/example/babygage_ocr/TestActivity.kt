package com.example.babygage_ocr

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.babygage_ocr.databinding.ActivityTestBinding
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.regex.Pattern


class TestActivity : AppCompatActivity() {
    val SELECT_MULTIPLE_IMAGES = 1
    var selectedImagesPaths // Paths of the image selected by the user.
            : ArrayList<String?>? = null
    var imagesSelected = false // Whether the user selected at least an image or not.
    private var output: StringBuilder = StringBuilder()
    var date: String = ""
    var name: String =""
    var price: String =""
    var imageView: ImageView? = null
    var intendImagePath : String = ""
    var cameraPath : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test", "권한 설정 요청")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 2)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
        var binding : ActivityTestBinding
        binding = ActivityTestBinding.inflate(layoutInflater)

        // check it is from HouseholdmainFragment or FinancialmainFragment
        val receive_intent = intent
        val category = receive_intent.getStringExtra("category")


        val sharedPref = getSharedPreferences("uj",MODE_PRIVATE)
        var userId =  sharedPref.getString("userid", "")
        Log.d("test","shared preference test user id: ${userId}")
        setContentView(binding.root)

        imageView =binding.ivMain
        // 툴바 생성
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)



        binding.btnConfirm.setOnClickListener {

            // 데이터 첨부를하고 액티비티 실행
            val temp: String =date
            val temp2: String = name
            val temp3: String = price

            val nextScreen = Intent(this, SetImageActivity::class.java)
            nextScreen.putExtra("key01", temp)
            nextScreen.putExtra("key02", temp2)
            nextScreen.putExtra("key03", temp3)
            nextScreen.putExtra("category",category)

            if (intendImagePath.length > 0) { // if there exists image path
                nextScreen!!.putExtra("path", intendImagePath)
            }
            startActivity(nextScreen)
        }




        binding.camera.setOnClickListener{
            val nextScreen = Intent(this, CameraActivity::class.java)
            startActivity(nextScreen)
        }

        // glide camera image
        cameraPath = intent.getStringExtra("camerapath").toString()
        Glide.with(this).load(cameraPath)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fallback(R.drawable.logo)
            .error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(imageView!!)
        Log.d("test","image view loaded")
        intendImagePath = cameraPath


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return
            }
            2 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Access to Internet Permission Denied.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    fun connectServer(v: View?) {
        val responseText = findViewById<TextView>(R.id.responseText)
        if (imagesSelected == false) { // This means no image is selected and thus nothing to upload.
            responseText.text = "No Image Selected to Upload. Select Image and Try Again."
            return
        }
        responseText.text = "Sending the Files. Please Wait ..."

//        val postUrl = "http://$ipv4Address:$portNumber/getpost"
        val postUrl =  "https://b026-203-246-85-178.jp.ngrok.io/getpost"
        Log.d("test","Post url: ${postUrl}")
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (i in selectedImagesPaths!!.indices) {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.RGB_565
            val stream = ByteArrayOutputStream()
            try {
                // Read BitMap by file path.
                val bitmap = BitmapFactory.decodeFile(selectedImagesPaths!![i], options)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            } catch (e: Exception) {
                responseText.text = "Please Make Sure the Selected File is an Image."
                return
            }
            val byteArray = stream.toByteArray()
            multipartBodyBuilder.addFormDataPart(
                "image$i", "Android_Flask_$i.jpg", RequestBody.create(
                    MediaType.parse("image/*jpg"), byteArray
                )
            )
        }
        val postBodyImage: RequestBody = multipartBodyBuilder.build()
        Log.d("test","postbody image: ${postBodyImage}")
        postRequest(postUrl, postBodyImage)
    }

    fun postRequest(postUrl: String?, postBody: RequestBody?) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(postUrl)
            .post(postBody)
            .build()
        Log.d("test","request: ${request}")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Cancel the post on failure.
                call.cancel()
                Log.d("test", e.message!!)

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread {
                    val responseText = findViewById<TextView>(R.id.responseText)
                    responseText.text = "Failed to Connect to Server. Please Try Again."
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.d("test","not failed!")
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread {
                    val responseText = findViewById<TextView>(R.id.responseText)
                    try {

                        //=============== get date, name, price information =================
                        val jsonObject = JSONObject(response.body().string())
                        date = jsonObject.getString("date")
                        name = jsonObject.getString("name")
                        price = jsonObject.getString("price")
                        responseText.text = "date: ${date}, name: ${name}, price: ${price}"
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    fun selectImage(v: View?) {
        val intent = Intent()
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            SELECT_MULTIPLE_IMAGES
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (requestCode == SELECT_MULTIPLE_IMAGES && resultCode == RESULT_OK && null != data) {
                // When a single image is selected.
                var currentImagePath: String?
                selectedImagesPaths = ArrayList()
                val numSelectedImages = findViewById<TextView>(R.id.numSelectedImages)
                if (data.data != null) {
                    val uri = data.data
                    currentImagePath = getPath(applicationContext, uri)
                    Log.d("test", "Single Image URI : $uri")
                    Log.d("test", "Single Image Path : $currentImagePath")
                    selectedImagesPaths!!.add(currentImagePath)
                    imagesSelected = true
                    numSelectedImages.text =
                        "Number of Selected Images : " + selectedImagesPaths!!.size
                    //SHOW IMAGE

                    if (currentImagePath!!.length > 0) {
                        intendImagePath = currentImagePath
                        imageView?.let {
                            Glide.with(this)
                                .load(currentImagePath!!)
                                .into(it)
                            Log.d("test","$currentImagePath!!")

                        }

                    }

                } else {
                    // When multiple images are selected.
                    // Thanks tp Laith Mihyar for this Stackoverflow answer : https://stackoverflow.com/a/34047251/5426539
                    if (data.clipData != null) {
                        val clipData = data.clipData
                        for (i in 0 until clipData!!.itemCount) {
                            val item = clipData.getItemAt(i)
                            val uri = item.uri
                            currentImagePath = getPath(applicationContext, uri)
                            selectedImagesPaths!!.add(currentImagePath)
                            Log.d("test", "Image URI $i = $uri")
                            Log.d("test", "Image Path $i = $currentImagePath")
                            imagesSelected = true
                            numSelectedImages.text =
                                "Number of Selected Images : " + selectedImagesPaths!!.size

//
//                            if (currentImagePath.isNotEmpty()) {
//                                currentImagePath?.let {
//                                    Glide.with(this)
//                                        .load(currentImagePath)
//                                        .into(it)
//                                    Log.d("test","$currentImagePath")
//                                }
                        }
                    }
                }







            }else {
                Toast.makeText(this, "You haven't Picked any Image.", Toast.LENGTH_LONG).show()
            }
            Toast.makeText(
                applicationContext,
                selectedImagesPaths!!.size.toString() + " Image(s) Selected.",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val IP_ADDRESS = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))"
        )

        // Implementation of the getPath() method and all its requirements is taken from the StackOverflow Paul Burke's answer: https://stackoverflow.com/a/20559175/5426539
        fun getPath(context: Context, uri: Uri?): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                    // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        if ("primary".equals(type, ignoreCase = true)) {
                            return Environment.getExternalStorageDirectory()
                                .toString() + "/" + split[1]
                        }

                        // TODO handle non-primary volumes
                    } else if (isDownloadsDocument(uri)) {
                        val id = DocumentsContract.getDocumentId(uri)
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
                        return getDataColumn(context, contentUri, null, null)
                    } else if (isMediaDocument(uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        if ("image" == type) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        } else if ("video" == type) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        } else if ("audio" == type) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(
                            split[1]
                        )
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
                    return getDataColumn(context, uri, null, null)
                } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                    return uri.path
                }
            }
            return null
        }

        fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection, selection, selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        fun isExternalStorageDocument(uri: Uri?): Boolean {
            return "com.android.externalstorage.documents" == uri!!.authority
        }

        fun isDownloadsDocument(uri: Uri?): Boolean {
            return "com.android.providers.downloads.documents" == uri!!.authority
        }

        fun isMediaDocument(uri: Uri?): Boolean {
            return "com.android.providers.media.documents" == uri!!.authority
        }

    }
}