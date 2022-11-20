package com.zaahid.challenge8.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.work.WorkInfo
import com.firebase.ui.auth.AuthUI
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zaahid.challenge8.ui.MainActivity
import com.zaahid.challenge8.ui.UserViewModel
import com.zaahid.challenge8.utils.Utils
import com.zaahid.challenge8.worker.KEY_IMAGE_URI
import com.zaahid.challenge8.R
import com.zaahid.challenge8.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

const val REQUEST_CODE_PERMISSION = 201
@Suppress("SameParameterValue", "DEPRECATION")
@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var analytics : FirebaseAnalytics
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.logoutButton.setOnClickListener {
            AuthUI.getInstance().signOut(this)
            updateUI(null)
            val intent = Intent(baseContext, MainActivity::class.java)
            Toast.makeText(baseContext, "Good Bye", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
        binding.buttonUpload.setOnClickListener {
            checkingPermission()
            chooseImageDialog()
        }
        userViewModel.getImageString().observe(this){
            if (it.isNullOrEmpty().not()){
                val bitmap : Bitmap = Utils.convertStringToBitmap(it)
                binding.imageProfile.setImageBitmap(bitmap)
                binding.imagedelete.visibility = View.VISIBLE
            }else{
                binding.imageProfile.setImageResource(R.drawable.ic_account_box)
                binding.imagedelete.visibility = View.GONE
            }
        }
        binding.imagedelete.setOnClickListener {
            userViewModel.setImageString("")
            userViewModel.setImageUri(null)
            userViewModel.setOutputUri(null)
        }
        binding.changeButton.setOnClickListener {
                val email = auth.currentUser?.email
                auth.currentUser?.email?.let { it1 ->
                    Firebase.auth.sendPasswordResetEmail(it1)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "email has been sended to $email", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
        }
        userViewModel.outputWorksInfos.observe(this,workInfosObserver())
        cekstatus()
        updateUI(auth.currentUser)
    }

    override fun onResume() {
        super.onResume()
        cekstatus()
    }
    private fun cekstatus(){
        if (auth.currentUser == null){
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null){
            binding.textViewid.text = user.uid
            binding.textViewemail.text = user.email
        }
    }
    private fun checkingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isGranted(
                    this, Manifest.permission.CAMERA,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    ), REQUEST_CODE_PERMISSION
                )
        }else{
            isGranted(
                this, Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }

    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(this)
            .setMessage("Pilih Gambar")
            .setPositiveButton("galeri") { _, _ -> openGalery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }
    private val cameraresult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        userViewModel.setImageString(Utils.bitMapToString(bitmap))
    }

    private fun openCamera() {
//        val photoFile = File.createTempFile("IMG_",".JPG",requireContext().getExternalFilesDir(
//            Environment.DIRECTORY_PICTURES))
//        val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider",photoFile)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraresult.launch(cameraIntent)
    }

    private val galeryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
//            binding.imageProfile.setImageURI(result)
            userViewModel.setImageUri(result)
            userViewModel.setImageString(
                Utils.bitMapToString(
                    MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        result
                    )
                )
            )
            userViewModel.applyBlur(2)
        }

    private fun openGalery() {
        this.intent.type = "image/*"
        galeryResult.launch("image/*")
    }
    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is Denied, pleas Allow Permission from settings")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                val uri = Uri.fromParts("package",packageName,null)
//                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancecl") { dialog, _ -> dialog.cancel() }
            .show()
    }
    private fun workInfosObserver(): androidx.lifecycle.Observer<List<WorkInfo>> {
        return androidx.lifecycle.Observer { listOfWorkInfo ->
            if (listOfWorkInfo.isNullOrEmpty()){
                return@Observer
            }
            val workInfo=listOfWorkInfo[0]
            if (workInfo.state.isFinished){
                showWorkFinished()
                val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)
                if (!outputImageUri.isNullOrEmpty()){
                    if (userViewModel.imageUri !=null){
                        userViewModel.setOutputUri(Utils.uriOrNull(outputImageUri))
                    }else{userViewModel.cancelWork()}
//                    userViewModel.outputUri.let {
//                        binding.imageProfile.setImageURI(it)
//                    }
                    userViewModel.outputUri?.let {
                        userViewModel.setImageString(
                            Utils.bitMapToString(MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            it
                        )))
                    }
//                    userViewModel.setImageString(Utils.bitMapToString(MediaStore.Images.Media.getBitmap(
//                        requireContext().contentResolver,
//                        Utils.uriOrNull(outputImageUri)
//                    )))
                }
            }else{
                showWorkInProgress()
            }
        }
    }
    private fun showWorkInProgress() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            button2.visibility = View.VISIBLE
            buttonUpload.visibility = View.GONE
        }
    }
    private fun showWorkFinished() {
        with(binding) {
            progressBar.visibility = View.GONE
            button2.visibility = View.GONE
            buttonUpload.visibility = View.VISIBLE
        }
    }
}