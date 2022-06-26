package project.whatsapp.whatsapp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import project.whatsapp.whatsapp.databinding.ActivityProfileBinding
import project.whatsapp.whatsapp.model.User
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var selectedImg: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.btnContinue.setOnClickListener {
            if (binding.edtUserName.text!!.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
                hideDefaultDialog(this)
            } else if (selectedImg == null) {
                Toast.makeText(this, "Please Select Your Image", Toast.LENGTH_SHORT).show()
            } else {
                showDefaultDialog(this)
                uploadData()
            }
        }


    }

    private fun uploadData() {
        val reference = storage.reference.child("Profile")
            .child(Date().time.toString())
        reference.putFile(selectedImg!!).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())

                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user = User(
            auth.uid.toString(),
            binding.edtUserName.text?.toString(),
            auth.currentUser!!.phoneNumber.toString(),
            imgUrl
        )

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                hideDefaultDialog(this)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Log.d("ProfileActivity", "Error : ${it.message}")
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.data != null) {
                    selectedImg = data.data!!
                    binding.userImage.setImageURI(selectedImg)
                }
            }
        }
    }

    private fun showDefaultDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            //setIcon(R.drawable.ic_hello)
            setTitle("Please Wait")
            setMessage("Creating User...")
            setCancelable(false)
        }.create().show()
    }

    private fun hideDefaultDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            //setIcon(R.drawable.ic_hello)
            setTitle("Creating User")
            setCancelable(false)
        }.create().hide()
    }


}