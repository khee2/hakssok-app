package com.android.hakssok

import android.app.DatePickerDialog
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
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.hakssok.databinding.ActivityMyProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyProfileBinding

    private val db = Firebase.firestore
    private val storage = Firebase.storage
    lateinit var filePath: String
    private var selectItem = ""
    private var nickname = ""
    private var birth = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileShow()
        binding.college.hint = LoginApp.college.toString()
        val college = resources.getStringArray(R.array.college)
        val collegeAdapter = ArrayAdapter(
            this,
            R.layout.item_list, college
        )
        binding.college.setAdapter(collegeAdapter)
        binding.college.setOnItemClickListener { adapterView, view, position, id ->
            selectItem = adapterView.getItemAtPosition(position) as String
        }

        binding.changeBirth.setOnClickListener {
            // 초기값으로 오늘 날짜 보여주기
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            // DatePickerDialog 생성
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    birth = "${selectedYear}/ ${selectedMonth + 1}/ ${selectedDayOfMonth}"
                    binding.birth.text = Editable.Factory.getInstance()
                        .newEditable(birth) // Editable 형식으로 값을 설정
                },
                year,
                month,
                dayOfMonth
            )
            // DatePickerDialog 띄우기
            datePickerDialog.show()
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

                bitmap?.let {
                    binding.profileImageView.setImageBitmap(bitmap)
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
                binding.profileImageView.setImageBitmap(bitmap)
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
        binding.ProfileSumbit.setOnClickListener {
            // 확인 메시지 표시
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
            alertDialogBuilder
                .setMessage("프로필을 수정하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    val nickname = binding.nickname.text.toString()
                    // fb storge에 이미지 업로드
                    if (binding.profileImageView != null && binding.profileImageView.drawable is BitmapDrawable) {
                        val image: Drawable = binding.profileImageView.drawable
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
                            SimpleDateFormat(
                                "yyyyMMddHHmmssSSS",
                                Locale.getDefault()
                            ).format(Date())
                        val fileName = "profileImage/${timestamp}.jpg"
                        val bitmapRef = storageRef.child(fileName)

                        val uploadTask: UploadTask = bitmapRef.putBytes(data)
                        uploadTask.addOnSuccessListener {
                            bitmapRef.downloadUrl.addOnSuccessListener {
                                var profileImageURL = it.toString()
                                Log.d("profileImageOk????!", profileImageURL)
                                profileLoad(profileImageURL, birth, selectItem, nickname)
                            }
                        }
                    } else {
                        profileLoad("", birth, selectItem, nickname)
                    }
                }
                .setNegativeButton("아니오") { _, _ -> }
                .show()
        }

        binding.reviewBackBtn.setOnClickListener {
            val myIntent: Intent = Intent(
                this@MyProfileActivity,
                RealActivity::class.java
            )
            startActivity(myIntent)
        }
    }

    // db의 user문서에 profile update
    private fun profileLoad(
        profileImageURL: String,
        birth: String,
        selectItem: String,
        nickname: String
    ) {
        Log.d("name은?!", nickname)
        Log.d("college는?!", selectItem)
        Log.d("profileImage는?!", profileImageURL)
        Log.d("birthDate?!", birth)
        db.collection("user").document(LoginApp.id!!)
            .update(
                mapOf(
                    "name" to nickname,
                    "college" to selectItem,
                    "birth" to birth,
                    "profileImage" to profileImageURL
                ),
            )
            .addOnSuccessListener {
                LoginApp.username = nickname
                LoginApp.college = selectItem
                LoginApp.birth = birth
                LoginApp.profileImage = profileImageURL
                Log.d("수정 성공!", "성공함.")
                Toast.makeText(this, "프로필이 수정되었습니다!", Toast.LENGTH_LONG).show()
                profileShow()
            }
            .addOnFailureListener { e ->
                Log.w(
                    "실패",
                    "Error writing document", e
                )
            }
    }

    // db에 저장되어 있는 사용자 정보 가져와서 보여주기
    private fun profileShow() {
        binding.nickname.text =
            Editable.Factory.getInstance()
                .newEditable(LoginApp.username.toString()) // Editable 형식으로 값을 설정
        binding.college.hint = LoginApp.college.toString()
        if (LoginApp.birth != null) { // 생일 정보가 없는 경우
            binding.birth.text = Editable.Factory.getInstance()
                .newEditable(LoginApp.birth.toString())
        }

        if (LoginApp.profileImage == null) { // 프로필 이미지가 없는 경우
            binding.profileImageView.setImageResource(R.drawable.profile_image) // 기본 이미지
        } else {
            Glide.with(binding.root.context).load(LoginApp.profileImage)
//                .apply(RequestOptions.bitmapTransform(RoundedCorners(20))) // 사진 테두리
                .into(binding.profileImageView)
        }
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산
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