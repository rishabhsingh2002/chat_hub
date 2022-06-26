package project.whatsapp.whatsapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import project.whatsapp.whatsapp.databinding.ActivityOtpactivityBinding
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please Wait")
        builder.setMessage("Verifying...")
        builder.setCancelable(false)

        alertDialog = builder.create()

        alertDialog.show()

        val phoneNumber = "+91" + intent.getStringExtra("number")

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    alertDialog.dismiss()
                    Toast.makeText(this@OTPActivity, "Please try again", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    alertDialog.dismiss()
                    verificationId = p0
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.btnEnter.setOnClickListener {

            if (binding.edtOtp.text!!.isNotEmpty()) {

                val credential =
                    PhoneAuthProvider.getCredential(
                        verificationId,
                        binding.edtOtp.text!!.toString()
                    )

                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //  dialog.show()
                            startActivity(Intent(this, ProfileActivity::class.java))
                            finish()
                        } else {
                            alertDialog.dismiss()
                            Toast.makeText(this, "Error : ${task.exception}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

            } else {

                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }

        }


    }

}