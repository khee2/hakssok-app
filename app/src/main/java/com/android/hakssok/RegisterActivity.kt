package com.android.hakssok

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.hakssok.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import org.checkerframework.checker.units.qual.s
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
//    private var star_count = 0

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    private var bitmap: Bitmap? = null
    lateinit var filePath: String

    private var storeId = ""

    //    private var uploadOk = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeId = intent.getStringExtra("storeId").toString()

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
//            star_count = rating.toInt()
            binding.ratingValue.text = rating.toInt().toString()
        }
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        {
            try {
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null

                bitmap?.let {
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let {
                    Log.d("hakssok", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun doTakeAlbumAction() {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.userImageView.setImageBitmap(bitmap)
            }
        }

        fun doTakePhotoAction() {
            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            filePath = file.absolutePath
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.android.hakssok.fileprovider",
                file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            requestCameraFileLauncher.launch(intent)
        }

        val cameraListener = DialogInterface.OnClickListener { _, _ -> doTakePhotoAction() }
        val albumListener = DialogInterface.OnClickListener { _, _ -> doTakeAlbumAction() }
        val cancelListener = DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() }

        binding.pictureButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("원하는 앱을 선택해 주세요")
                .setIcon(android.R.drawable.ic_menu_camera)
                .setNegativeButton("카메라 앱", cameraListener)
                .setPositiveButton("사진 앱", albumListener)
                .setNeutralButton("취소", cancelListener)
                .show()
        }

        binding.reviewSumbit.setOnClickListener {
            // fb storge에 이미지 업로드
            if (binding.userImageView != null && binding.userImageView.drawable is BitmapDrawable) {
                val image: Drawable = binding.userImageView.drawable
                val bitmap: Bitmap = (image as BitmapDrawable).bitmap

                val baos = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, baos)
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                }
                val data = baos.toByteArray()

                val storageRef = storage.reference
                val timestamp =
                    SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(Date())
                val fileName = "image/$timestamp.jpg"
                val bitmapRef = storageRef.child(fileName)

                val uploadTask: UploadTask = bitmapRef.putBytes(data)
                uploadTask.addOnSuccessListener {
                    bitmapRef.downloadUrl.addOnSuccessListener {
                        var reviewImageURL = it.toString()
                        Log.d("reviewImageUR11L", reviewImageURL)
                        ReviewLoad(reviewImageURL)
                    }
                }
            }
            else {
                ReviewLoad("")
            }
        }

        binding.reviewBackBtn.setOnClickListener {
            val myIntent: Intent = Intent(
                this@RegisterActivity,
                DetailActivity::class.java
            )
            startActivity(myIntent)
        }
    }
        fun ReviewLoad(reviewImageURL: String){
            val reviewText = binding.reviewText.text.toString()
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
            val formattedDateTime = currentDateTime.format(formatter)
            var starCount = binding.ratingValue.text
            val registerInfo = hashMapOf(
                "date" to formattedDateTime,
                "picture" to reviewImageURL,
                "review" to reviewText,
                "storeId" to storeId,
                "score" to starCount,
                "userName" to LoginApp.username
            )
            db.collection("user")
                .document(formattedDateTime)
                .set(registerInfo)
                .addOnSuccessListener {
                    val myIntent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(myIntent)
                }
        }

        private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            try {
                var inputStream = contentResolver.openInputStream(fileUri)

                //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
                //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
                BitmapFactory.decodeStream(inputStream, null, options)
                inputStream!!.close()
                inputStream = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //비율 계산........................
            val (height: Int, width: Int) = options.run { outHeight to outWidth }
            var inSampleSize = 1
            //inSampleSize 비율 계산
            if (height > reqHeight || width > reqWidth) {

                val halfHeight: Int = height / 2
                val halfWidth: Int = width / 2

                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }
    }