package project.whatsapp.whatsapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import project.whatsapp.whatsapp.databinding.ActivityNumberBinding

class NumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnContinue.setOnClickListener {
            if (binding.edtNumber.text!!.isNotEmpty()) {
                val intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("number", binding.edtNumber.text!!.toString())
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Please Enter Your Valid Number", Toast.LENGTH_SHORT).show()
            }
        }


    }
}